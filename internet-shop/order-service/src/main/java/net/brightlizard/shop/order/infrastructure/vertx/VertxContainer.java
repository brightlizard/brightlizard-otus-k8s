package net.brightlizard.shop.order.infrastructure.vertx;

import com.hazelcast.config.Config;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Service
@DependsOn("springVerticleFactory")
public class VertxContainer {

    public Logger LOGGER = LoggerFactory.getLogger(VertxContainer.class);

    private Vertx vertx;
    private int instances = VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE;
    private HashSet<String> depIds = new HashSet<>();
    private boolean ready = false;
    private boolean multicastEnabled;
    private boolean tcpipEnabled;
    private boolean kubernetesEnabled;
    private String kubernetesServiceDns;
    private SpringVerticleFactory springVerticleFactory;

    public VertxContainer(
        @Value("${ishop.hz.network.multicast.enabled:false}") boolean multicastEnabled,
        @Value("${ishop.hz.network.tcpip.enabled:false}") boolean tcpipEnabled,
        @Value("${ishop.hz.network.kubernetes.enabled:false}") boolean kubernetesEnabled,
        @Value("${ishop.hz.network.kubernetes.servicedns:k8s-dicovery-headless-service.otus.svc.cluster.local}") String kubernetesServiceDns,
        SpringVerticleFactory springVerticleFactory
    ) {
        this.multicastEnabled = multicastEnabled;
        this.tcpipEnabled = tcpipEnabled;
        this.kubernetesEnabled = kubernetesEnabled;
        this.kubernetesServiceDns = kubernetesServiceDns;
        this.springVerticleFactory = springVerticleFactory;

        LOGGER.info("Muslicast discovery -> {}", this.multicastEnabled);
        LOGGER.info("Tcp discovery -> {}", this.tcpipEnabled);
        LOGGER.info("Kubernetes discovery -> {}", this.kubernetesEnabled);
        LOGGER.info("Kubernetes service -> {}", this.kubernetesServiceDns);

        Config hazelcastConfig = new Config();

        hazelcastConfig.setClusterName("ishop");
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(this.multicastEnabled);
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(this.tcpipEnabled);
        hazelcastConfig.getNetworkConfig()
                .getJoin()
                .getKubernetesConfig()
                .setEnabled(this.kubernetesEnabled)
                .setProperty("service-dns", this.kubernetesServiceDns);

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                vertx = res.result();
                ready = true;
                doAfterClusteredVertxStarted();
            } else {
                LOGGER.info("FAILED TO START HAZELCAST!");
                res.cause().printStackTrace();
                throw new RuntimeException("CANT START HAZELCAST");
            }
        });
    }

    @PostConstruct
    private void init() throws InterruptedException {
        while (ready != true){
            LOGGER.info("Waiting for HZ is starting.");
            Thread.sleep(1000);
        }
    }

    private void doAfterClusteredVertxStarted(){
        vertx.registerVerticleFactory(springVerticleFactory);
        DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(instances);
        String orderReactorName = SpringVerticleFactory.VERTICLE_PREFIX + ':' + OrderReactor.class.getName();
        vertx.deployVerticle(orderReactorName, deploymentOptions, this::handleDeploymentId);
        LOGGER.info("Deployment IDs -> {}", getDepIds());
    }

    public Vertx getVertx() {
        return vertx;
    }

    private void handleDeploymentId(AsyncResult<String> res) {
        depIds.add(res.result());
    }

    @PreDestroy
    private void destroy() {
        vertx.undeploy(depIds.toString());
    }

    private HashSet<String> getDepIds() {
        return depIds;
    }

    public int getInstances() {
        return instances;
    }

}

package net.brightlizard.shop.core.application.configuration;

import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import net.brightlizard.shop.core.application.billing.BillingReactor;
import net.brightlizard.shop.core.application.delivery.DeliveryReactor;
import net.brightlizard.shop.core.application.order.OrderReactor;
import net.brightlizard.shop.core.application.payment.PaymentReactor;
import net.brightlizard.shop.core.application.storage.StorageReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PreDestroy;
import java.util.HashSet;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class VerticleDeployService {

    Logger LOGGER = LoggerFactory.getLogger(VerticleDeployService.class);

    private int instances = VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE;
    private HashSet<String> depIds = new HashSet<>();
    private Vertx vertx;

    public VerticleDeployService(@Autowired Vertx vertx) {
        this.vertx = vertx;

        DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(instances);

        String orderReactorName = SpringVerticleFactory.VERTICLE_PREFIX + ':' + OrderReactor.class.getName();
        vertx.deployVerticle(orderReactorName, deploymentOptions, this::handleDeploymentId);
        LOGGER.info("Deployment IDs -> {}", getDepIds());

        String storageReactorName = SpringVerticleFactory.VERTICLE_PREFIX + ':' + StorageReactor.class.getName();
        vertx.deployVerticle(storageReactorName, deploymentOptions, this::handleDeploymentId);
        LOGGER.info("Deployment IDs -> {}", getDepIds());

        String paymentReactorName = SpringVerticleFactory.VERTICLE_PREFIX + ':' + PaymentReactor.class.getName();
        vertx.deployVerticle(paymentReactorName, deploymentOptions, this::handleDeploymentId);
        LOGGER.info("Deployment IDs -> {}", getDepIds());

        String deliveryReactorName = SpringVerticleFactory.VERTICLE_PREFIX + ':' + DeliveryReactor.class.getName();
        vertx.deployVerticle(deliveryReactorName, deploymentOptions, this::handleDeploymentId);
        LOGGER.info("Deployment IDs -> {}", getDepIds());

        String billingReactorName = SpringVerticleFactory.VERTICLE_PREFIX + ':' + BillingReactor.class.getName();
        vertx.deployVerticle(billingReactorName, deploymentOptions, this::handleDeploymentId);
        LOGGER.info("Deployment IDs -> {}", getDepIds());
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

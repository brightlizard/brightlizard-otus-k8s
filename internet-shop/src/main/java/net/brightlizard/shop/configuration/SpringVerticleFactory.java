package net.brightlizard.shop.configuration;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.concurrent.Callable;

@Component
public class SpringVerticleFactory implements VerticleFactory {

    public static final String VERTICLE_PREFIX = "spring";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String prefix() {
        return VERTICLE_PREFIX;
    }

    @Override
    public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
        String clazz = VerticleFactory.removePrefix(verticleName);
        promise.complete(() -> (Verticle) applicationContext.getBean(Class.forName(clazz)));

    }
}

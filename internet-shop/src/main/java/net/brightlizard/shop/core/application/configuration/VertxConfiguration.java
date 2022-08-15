package net.brightlizard.shop.core.application.configuration;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxConfiguration {

    @Bean
    public Vertx vertx(@Autowired SpringVerticleFactory springVerticleFactory){
        Vertx vertx = Vertx.vertx();
        vertx.registerVerticleFactory(springVerticleFactory);
        return vertx;
    }

    @Bean
    public SpringVerticleFactory springVerticleFactory() {
        return new SpringVerticleFactory();
    }

}

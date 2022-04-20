package net.brightlizard.otus.k8s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package net.brightlizard.otus.k8s.config;

import net.brightlizard.otus.k8s.rest.MetricsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private MetricsInterceptor metricsInterceptor;

    public WebConfiguration(@Autowired MetricsInterceptor metricsInterceptor) {
        this.metricsInterceptor = metricsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(metricsInterceptor);
    }
}

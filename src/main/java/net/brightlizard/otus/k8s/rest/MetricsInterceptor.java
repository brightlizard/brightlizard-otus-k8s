package net.brightlizard.otus.k8s.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Component
public class MetricsInterceptor implements HandlerInterceptor {

    private static Logger LOGGER = LoggerFactory.getLogger(MetricsInterceptor.class);
    private MeterRegistry meterRegistry;
    private ConcurrentHashMap<String, Long> startTimeByPath = new ConcurrentHashMap<>();
    private HashMap<String, Counter> counterByPath = new HashMap<>();
    private HashMap<String, DistributionSummary> summaryByPath = new HashMap<>();

    public MetricsInterceptor (
        @Autowired MeterRegistry meterRegistry
    ) {
        this.meterRegistry = meterRegistry;
    }

    private String getRequestIndex(HttpServletRequest request) {
        return String.format("%s%s", request.getMethod(), request.getRequestURI());
    }

    private String getResponseIndex(HttpServletRequest request, HttpServletResponse response) {
        return String.format("%s%s:%s", request.getMethod(), request.getRequestURI(), response.getStatus());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIndex = getRequestIndex(request);
        LOGGER.info("PRE HANDLE INTERCEPTOR");
        startTimeByPath.put(requestIndex, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOGGER.info("POST HANDLE INTERCEPTOR");
        String requestIndex = getRequestIndex(request);
        String responseIndex = getResponseIndex(request, response);
        counterByPath.computeIfAbsent(responseIndex, idx -> {
            return meterRegistry.counter(
                    "requests.counter",
                    List.of(
                        Tag.of("method", request.getMethod()),
                        Tag.of("uri", request.getRequestURI()),
                        Tag.of("code", Integer.toString(response.getStatus()))
                    )
                );
        });
        counterByPath.get(responseIndex).increment();
        summaryByPath.computeIfAbsent(responseIndex, idx -> {
            return DistributionSummary
                    .builder("requests.latency")
                    .baseUnit("ms")
                    .tags(List.of(Tag.of("method", request.getMethod()), Tag.of("uri", request.getRequestURI()), Tag.of("code", Integer.toString(response.getStatus()))))
                    .publishPercentiles(0.5, 0.95, 0.99)
                    .publishPercentileHistogram()
                    .register(meterRegistry);
        });
        int delay = new Random().nextInt(1000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long latency = (System.currentTimeMillis() - startTimeByPath.get(requestIndex));
        System.out.println("LATENCY = " + latency);
        startTimeByPath.remove(requestIndex);
        summaryByPath.get(responseIndex).record(latency);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOGGER.info("AFTER COMPLETION INTERCEPTOR");
    }
}

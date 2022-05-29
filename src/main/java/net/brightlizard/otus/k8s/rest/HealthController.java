package net.brightlizard.otus.k8s.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
public class HealthController {

    private boolean isHealthEnabled = true;
    private final Counter healthCounter;
    private MeterRegistry meterRegistry;

    public HealthController(@Autowired MeterRegistry meterRegistry) {

        this.healthCounter = meterRegistry.counter("health.counter", List.of(Tag.of("method", "GET"), Tag.of("uri", "/health")));
        this.meterRegistry = meterRegistry;
    }

    @GetMapping(
        value = "/health",
        produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> health(){
        long start = System.currentTimeMillis();
        if(isHealthEnabled) {
            int delay = new Random().nextInt(100);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.healthCounter.increment();
            DistributionSummary latencySummary = DistributionSummary
                    .builder("request.latency")
                    .baseUnit("ms")
                    .tags(List.of(Tag.of("method", "GET"), Tag.of("uri", "/health")))
                    .publishPercentiles(0.5, 0.95, 0.99)
                    .publishPercentileHistogram()
                    .register(meterRegistry);
            long latency = (System.currentTimeMillis() - start);
            System.out.println("LATENCY = " + latency);
            latencySummary.record(latency);
            return new ResponseEntity<>("{\"status\": \"OK\"}", HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(
            value = "/health/on",
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public String healthOn(){
        isHealthEnabled = true;
        return "{\"status\": \"DONE\"}";
    }

    @GetMapping(
            value = "/health/off",
            produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public String healthOff(){
        isHealthEnabled = false;
        return "{\"status\": \"DONE\"}";
    }
}

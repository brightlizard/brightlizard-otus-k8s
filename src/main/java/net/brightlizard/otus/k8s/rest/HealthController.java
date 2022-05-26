package net.brightlizard.otus.k8s.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
public class HealthController {

    private boolean isHealthEnabled = true;
    private final Counter healthCounter;

    public HealthController(@Autowired MeterRegistry meterRegistry) {
        this.healthCounter = meterRegistry.counter("health.counter");
    }

    @GetMapping(
        value = "/health",
        produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> health(){
        if(isHealthEnabled) {
            this.healthCounter.increment();
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

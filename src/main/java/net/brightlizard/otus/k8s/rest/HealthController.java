package net.brightlizard.otus.k8s.rest;

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

    @GetMapping(
        value = "/health",
        produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> health(){
        if(isHealthEnabled) return new ResponseEntity<>("{\"status\": \"OK\"}", HttpStatus.OK);
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

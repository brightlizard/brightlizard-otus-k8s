package net.brightlizard.otus.k8s.rest;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
public class HealthController {

    @GetMapping(
        value = "/health",
        produces = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public String health(){
        return "{\"status\": \"OK\"}";
    }
}

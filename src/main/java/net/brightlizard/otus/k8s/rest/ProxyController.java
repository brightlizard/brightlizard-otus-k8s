package net.brightlizard.otus.k8s.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/**", consumes = MediaType.ALL_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<String> proxy(@RequestParam String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).stream().forEach(name -> headers.set(name, request.getHeader(name)));
        HttpEntity<Void> entity = new HttpEntity<Void>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), entity, String.class, new Object());
        return response;
    }

}

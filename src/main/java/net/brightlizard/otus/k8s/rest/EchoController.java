package net.brightlizard.otus.k8s.rest;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
@RequestMapping("/echo")
public class EchoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoController.class);

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/**", consumes = MediaType.ALL_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<String> echo(@RequestBody(required = false) byte[] rawBody){

        final Map<String, String> headers  = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(Function.identity(), request::getHeader));

        final JsonPayload response = new JsonPayload();
        response.set(JsonPayload.PROTOCOL, request.getProtocol());
        response.set(JsonPayload.METHOD, request.getMethod());
        response.set(JsonPayload.HEADERS, headers);
        response.set(JsonPayload.COOKIES, request.getCookies());
        response.set(JsonPayload.PARAMETERS, request.getParameterMap());
        response.set(JsonPayload.PATH, request.getServletPath());
        response.set(JsonPayload.BODY, rawBody != null ? Base64.getEncoder().encodeToString(rawBody) : null);
        LOGGER.info("REQUEST: {}", request.getParameterMap());

        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class JsonPayload {

        public static final String PROTOCOL = "protocol";

        public static final String METHOD = "method";

        public static final String HEADERS = "headers";

        public static final String COOKIES = "cookies";

        public static final String PARAMETERS = "parameters";

        public static final String PATH = "path";

        public static final String BODY = "body";

        private final Map<String, Object> attributes = new HashMap<>();

        @JsonAnySetter
        public void set(String name, Object value) {
            attributes.put(name, value);
        }

        @JsonAnyGetter
        public Map<String, Object> getAttributes() {
            return attributes;
        }

    }

}

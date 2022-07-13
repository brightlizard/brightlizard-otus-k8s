package net.brightlizard.otus.k8s.rest;

import net.brightlizard.otus.k8s.model.Item;
import net.brightlizard.otus.k8s.model.Order;
import net.brightlizard.otus.k8s.openapi.api.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@RestController
@RequestMapping("/order")
public class OrderController implements UserApi {

    private Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    HashMap<String, Order> requestToOrder = new HashMap<>();

    public OrderController() {

    }

    @PostMapping(
        produces = MimeTypeUtils.APPLICATION_JSON_VALUE,
        consumes = MimeTypeUtils.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Order> createOrder(
        @RequestHeader("X-Request-Id") String xRequestId,
        @RequestBody @Valid Order order
    ){
        LOGGER.info("X-Request-Header", xRequestId);
        LOGGER.info("BODY", order);

        if (requestToOrder.keySet().contains(xRequestId)) {
            return new ResponseEntity<>(requestToOrder.get(xRequestId), HttpStatus.OK);
        }

        double totalSum = order.getItems().stream().mapToDouble(i -> i.getPrice()).sum();
        order.setTotalSum(totalSum);
        order.setId(UUID.randomUUID().toString());

        requestToOrder.put(xRequestId, order);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

}

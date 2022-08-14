package net.brightlizard.shop.infrastructure.rest;

import net.brightlizard.shop.core.application.OrderFacade;
import net.brightlizard.shop.core.application.order.model.RequestStatus;
import net.brightlizard.shop.infrastructure.rest.model.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */

@RestController
public class OrderController {

    private OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping(
        value = "/order",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity createOrder(
        @RequestHeader("X-Request-Id") String xRequestId,
        @RequestBody @Valid Order order
    ){
        RequestStatus requestStatus = orderFacade.handleRequest(order);
        if(
            requestStatus.equals(RequestStatus.CREATED) ||
            requestStatus.equals(RequestStatus.ALREADY_EXIST)
        ){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/results"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        return new ResponseEntity("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(
        value = "/results",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity getResults(){


        return new ResponseEntity(HttpStatus.OK);
    }
}

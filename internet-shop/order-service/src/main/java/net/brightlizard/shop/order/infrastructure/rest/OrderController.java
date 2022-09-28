package net.brightlizard.shop.order.infrastructure.rest;

import net.brightlizard.shop.order.core.facade.OrderFacade;
import net.brightlizard.shop.order.core.model.RequestStatus;
import net.brightlizard.shop.order.core.model.ShortItem;
import net.brightlizard.shop.order.infrastructure.rest.model.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity createOrder(
        @RequestHeader("X-Request-Id") String xRequestId,
        @Valid @RequestBody Order order
    ){
        RequestStatus requestStatus = orderFacade.handleRequest(convert(xRequestId, order));
        if(
            requestStatus.equals(RequestStatus.CREATED) ||
            requestStatus.equals(RequestStatus.ALREADY_EXIST)
        ){
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/order/results"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        return new ResponseEntity("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(
        value = "/results",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Order>> getResults() {
        return new ResponseEntity(orderFacade.getResults(), HttpStatus.OK);
    }

    @GetMapping(
        value = "/requests",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Order>> getRequests() {
        return new ResponseEntity(orderFacade.getRequests(), HttpStatus.OK);
    }

    public net.brightlizard.shop.order.core.model.Order convert(String requestId, Order order){
        List<net.brightlizard.shop.order.core.model.ShortItem> targetItems = new ArrayList<>();
        order.getItems().forEach(shortItem -> {
            ShortItem targetItem = new ShortItem(shortItem.getId(), shortItem.getQuantity());
            targetItems.add(targetItem);
        });

        return new net.brightlizard.shop.order.core.model.Order(
                requestId, order.getConsumer(), targetItems, order.getDeliveryTime()
        );
    }

}

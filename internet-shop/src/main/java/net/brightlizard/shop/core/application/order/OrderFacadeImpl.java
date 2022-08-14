package net.brightlizard.shop.core.application.order;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderCreatedEvent;
import net.brightlizard.shop.core.application.order.model.RequestStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class OrderFacadeImpl implements OrderFacade {

    private HashMap<String, Order> orderRequest = new HashMap<>();
    private List orders = new ArrayList();
    private ApplicationEventPublisher eventPublisher;

    @Override
    public RequestStatus handleRequest(Order order) {
        try {
            if (orderRequest.containsKey(order.getRequestId())) {
                return RequestStatus.ALREADY_EXIST;
            }
            orderRequest.put(order.getRequestId(), order);

            // Order created event
            eventPublisher.publishEvent(new OrderCreatedEvent(order));


            return RequestStatus.CREATED;
        } catch (Exception e) {
            return RequestStatus.FAILED;
        }
    }

    @Override
    public List<Order> getResults() {

        return orders;
    }

}

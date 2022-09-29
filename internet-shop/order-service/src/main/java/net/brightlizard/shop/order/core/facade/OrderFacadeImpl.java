package net.brightlizard.shop.order.core.facade;

import net.brightlizard.shop.order.core.service.OrderService;
import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.OrderStatus;
import net.brightlizard.shop.order.core.model.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class OrderFacadeImpl implements OrderFacade {

    private Logger LOGGER = LoggerFactory.getLogger(OrderFacadeImpl.class);
    private OrderService orderService;

    public OrderFacadeImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public RequestStatus handleRequest(Order order) {
        try {
            RequestStatus requestStatus = orderService.createRequest(order);
            if (requestStatus.equals(RequestStatus.ALREADY_EXIST)) {
                LOGGER.info("ALREADY EXIST");
                return requestStatus;
            }

            OrderStatus orderStatus = orderService.createOrder(order);

            if(orderStatus.equals(OrderStatus.CREATED)){
                return requestStatus;
            }
            return RequestStatus.FAILED;
        } catch (Exception e) {
            return RequestStatus.FAILED;
        }
    }

    @Override
    public List<Order> getResults() {
        return orderService.getResults();
    }

    @Override
    public List<String> getRequests() {
        return orderService.getRequests();
    }

}

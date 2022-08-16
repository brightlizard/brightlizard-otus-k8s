package net.brightlizard.shop.core.application.order.facade;

import io.vertx.core.Vertx;
import net.brightlizard.shop.core.application.order.OrderService;
import net.brightlizard.shop.core.application.order.OrderServiceImpl;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.model.RequestStatus;
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
                LOGGER.info("ALREADY EXIST 2");
                return requestStatus;
            }

            LOGGER.info("CREATED 2");
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
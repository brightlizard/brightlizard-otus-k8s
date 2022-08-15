package net.brightlizard.shop.core.application.order;

import io.vertx.core.Vertx;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.model.RequestStatus;
import net.brightlizard.shop.core.application.order.repository.OrderRepository;
import net.brightlizard.shop.core.application.order.repository.OrderRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Vertx vertx;
    private OrderRequestRepository orderRequestRepository;
    private OrderRepository orderRepository;

    public OrderServiceImpl(Vertx vertx, OrderRequestRepository orderRequestRepository, OrderRepository orderRepository) {
        this.vertx = vertx;
        this.orderRequestRepository = orderRequestRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public RequestStatus createRequest(Order order) {
        if (orderRequestRepository.containsKey(order.getRequestId())) {
            return RequestStatus.ALREADY_EXIST;
        }
        orderRequestRepository.putRequestHash(order.getRequestId(), Integer.toString(order.hashCode()));
        return RequestStatus.CREATED;
    }

    @Override
    public OrderStatus createOrder(Order order) {
        try {
            order.setStatus(OrderStatus.CREATED);
            orderRepository.save(order);

            // Send order created event
            vertx.eventBus().send("order_created", SerializationUtils.serialize(order));

            return order.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
            return OrderStatus.FAILED;
        }
    }

    @Override
    public List<Order> getResults() {
        return orderRepository.findAll();
    }

    @Override
    public List<String> getRequests() {
        return orderRequestRepository.findAll();
    }

}

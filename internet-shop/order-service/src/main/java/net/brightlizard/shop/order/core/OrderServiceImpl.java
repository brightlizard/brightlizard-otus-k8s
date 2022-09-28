package net.brightlizard.shop.order.core;

import io.vertx.core.Vertx;
import net.brightlizard.shop.order.core.model.Item;
import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.OrderStatus;
import net.brightlizard.shop.order.core.model.RequestStatus;
import net.brightlizard.shop.order.core.repository.OrderRepository;
import net.brightlizard.shop.order.core.repository.OrderRequestRepository;
import net.brightlizard.shop.order.infrastructure.vertx.VertxContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private Vertx vertx;
    private OrderRequestRepository orderRequestRepository;
    private OrderRepository orderRepository;

    public OrderServiceImpl(VertxContainer vertxContainer, OrderRequestRepository orderRequestRepository, OrderRepository orderRepository) {
        this.vertx = vertxContainer.getVertx();
        this.orderRequestRepository = orderRequestRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public RequestStatus createRequest(Order order) {
        if (orderRequestRepository.containsKey(order.getRequestId())) {
            LOGGER.info("ALREADY EXIST");
            return RequestStatus.ALREADY_EXIST;
        }

        LOGGER.info("CREATED");
        orderRequestRepository.putRequestHash(order.getRequestId(), Integer.toString(order.hashCode()));
        return RequestStatus.CREATED;
    }

    @Override
    public OrderStatus createOrder(Order order) {
        try {
            order.setStatus(OrderStatus.CREATED);
            orderRepository.save(order);

            // Send order created event
            LOGGER.info("XXXXXXXXX1");
            vertx.eventBus().send("order_created", SerializationUtils.serialize(order));
            LOGGER.info("XXXXXXXXX2");

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

    @Override
    public Order calculateTotalSum(Order order) {
        double totalSum = order.getOrderedItems().stream().mapToDouble(Item::getPrice).sum();
        order.setTotalPrice(totalSum);
        return order;
    }

}

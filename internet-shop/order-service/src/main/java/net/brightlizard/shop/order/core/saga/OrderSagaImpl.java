package net.brightlizard.shop.order.core.saga;

import net.brightlizard.shop.event.model.notification.NotificationStatusEventModel;
import net.brightlizard.shop.order.core.events.EventProcessor;
import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.OrderStatus;
import net.brightlizard.shop.order.core.repository.OrderRepository;
import net.brightlizard.shop.order.core.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Service
@Lazy
public class OrderSagaImpl implements OrderSaga {

    private Logger LOGGER = LoggerFactory.getLogger(OrderSagaImpl.class);

    private EventProcessor eventProcessor;
    private OrderService orderService;
    private OrderRepository orderRepository;

    public OrderSagaImpl(
        @Lazy EventProcessor eventProcessor,
        @Lazy OrderService orderService,
        OrderRepository orderRepository
    ) {
        this.eventProcessor = eventProcessor;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order startFor(Order order) {
        order.setStatus(OrderStatus.CREATED);
        orderRepository.save(order);
        CompletableFuture.runAsync(() -> runStorageReserve(order));
        return order;
    }

    @Override
    public void runStorageReserve(Order order){
        eventProcessor.runStorageReserve(order, processedOrder -> {
            LOGGER.info("UPDATE ORDER AFTER RUN STORAGE RESERVE -> {}", processedOrder);
            orderRepository.updateCommStatus(processedOrder);
        });
    }

    @Override
    public void handleStorageReserveReply(Order order) {
        order = orderRepository.updateStatusAndItems(order);
        if(order.getStatus().equals(OrderStatus.STORAGE_RESERVE_SUCCESS)){
            runPaymentDo(preparePayment(order));
        }
    }

    @Override
    public void runPaymentDo(Order order) {
        eventProcessor.runPaymentDo(order, processedOrder -> {
            LOGGER.info("UPDATE ORDER AFTER RUN PAYMENT DO -> {}", processedOrder);
            orderRepository.updateCommStatus(processedOrder);
        });
    }

    @Override
    public void handlePaymentDoReply(Order order) {
        order = orderRepository.updateStatus(order);
        if(order.getStatus().equals(OrderStatus.PAYMENT_SUCCESS)){
            eventProcessor.runScheduleDelivery(order, processedOrder -> {
                LOGGER.info("UPDATE ORDER AFTER RUN SCHEDULE DELIVERY -> {}", processedOrder);
                orderRepository.updateCommStatus(processedOrder);
            });
            eventProcessor.runSendNotification(order, NotificationStatusEventModel.EVERYTHING_GOOD);
        }
        if(order.getStatus().equals(OrderStatus.PAYMENT_ERROR)){
            eventProcessor.runRollbackReservation(order, processedOrder -> {
                LOGGER.info("UPDATE ORDER AFTER RUN ROLLBACK RESERVATION -> {}", processedOrder);
                orderRepository.updateCommStatus(processedOrder);
            });
            eventProcessor.runSendNotification(order, NotificationStatusEventModel.ALL_BAD);
        }
    }

    @Override
    public void handleScheduleDeliveryReply(Order order) {
        order = orderRepository.updateStatus(order);
        if(order.getStatus().equals(OrderStatus.DELIVERY_SUCCESS)){
            LOGGER.info("COMPLETE SAGA FOR -> {}", order);
        }
        if(order.getStatus().equals(OrderStatus.DELIVERY_ERROR)){
            eventProcessor.runRollbackPayment(order, processedOrder -> {
                LOGGER.info("UPDATE ORDER AFTER SCHEDULE DELIVERY REPLY -> {}", processedOrder);
                orderRepository.updateCommStatus(processedOrder);
            });
            eventProcessor.runRollbackReservation(order, processedOrder -> orderRepository.updateStatus(processedOrder));
        }
    }

    @Override
    public void handlePaymentRollbackReply(Order order) {
        order = orderRepository.updateStatus(order);
        if(order.getStatus().equals(OrderStatus.PAYMENT_ROLLBACK)){
            LOGGER.info("PAYMENT ROLLBACK FOR -> {}", order);
            eventProcessor.runSendNotification(order, NotificationStatusEventModel.ALL_BAD);
        }
    }

    @Override
    public void handleReservationRollbackReply(Order order) {
        order = orderRepository.updateStatus(order);
        if (order.getStatus().equals(OrderStatus.STORAGE_RESERVE_ROLLBACK)) {
            LOGGER.info("ITEMS RESERVATION ROLLBACK FOR -> {}", order);
        }
    }

    private Order preparePayment(Order order) {
        Order calculatedOrder = orderService.calculateTotalSum(order);
        return calculatedOrder;
    }

}

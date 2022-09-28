package net.brightlizard.shop.order.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.event.model.notification.NotificationEventModel;
import net.brightlizard.shop.event.model.notification.NotificationStatusEventModel;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.order.core.converter.OrderEventModelConverter;
import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.OrderCommStatus;
import net.brightlizard.shop.order.core.model.OrderStatus;
import net.brightlizard.shop.order.core.repository.OrderRepository;
import net.brightlizard.shop.order.infrastructure.vertx.VertxContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 *
 * Order Actor
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderReactor extends AbstractVerticle {

    private Logger LOGGER = LoggerFactory.getLogger(OrderReactor.class);
    private Vertx vertx;
    private OrderEventModelConverter orderEventModelConverter;
    private EventBus eventBus;
    private OrderService orderService;
    private OrderRepository orderRepository;

    public OrderReactor(
            VertxContainer vertxContainer,
            OrderService orderService,
            OrderRepository orderRepository,
            OrderEventModelConverter orderEventModelConverter
    ) {
        this.vertx = vertxContainer.getVertx();
        this.orderEventModelConverter = orderEventModelConverter;
        this.eventBus = vertx.eventBus();
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("order_created", orderCreatedHandler()); // Begin Saga
        eventBus.consumer("storage_reserve_reply", storageReserveReplyHandler());
        eventBus.consumer("payment_do_reply", paymentDoReplyHandler());
        eventBus.consumer("schedule_delivery_reply", scheduleDeliveryReplyHandler());
        eventBus.consumer("payment_do_rollback_reply", paymentDoRollbackReplyHandler());
        eventBus.consumer("storage_reserve_rollback_reply", storageReserveRollbackReplyHandler());

        startPromise.complete();
    }

    private Handler<Message<Object>> orderCreatedHandler() {
        return message -> {
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
            eventBus.request("storage_reserve", SerializationUtils.serialize(orderEventModel), ar -> {
                if(ar.succeeded()){
                    LOGGER.info("ar.succeeded()");
                    order.setCommStatus(OrderCommStatus.SEND_REQ_TO_STORAGE_SUCCESS);
                }
                if(ar.failed()){
                    LOGGER.info("ar.failed()");
                    order.setCommStatus(OrderCommStatus.SEND_REQ_TO_STORAGE_ERROR);
                }
                orderRepository.update(order);
            });
        };
    }

    private Handler<Message<Object>> storageReserveReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            Order order = orderEventModelConverter.convert(orderEventModel);
            order = orderRepository.updateStatusAndItems(order);
            if(order.getStatus().equals(OrderStatus.STORAGE_RESERVE_SUCCESS)){
                doPaymentRequest(order);
            }
        };
    }

    private void doPaymentRequest(Order order) {
        Order calculatedOrder = orderService.calculateTotalSum(order);
        OrderEventModel orderEventModel = orderEventModelConverter.convert(calculatedOrder);
        eventBus.request("payment_do", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                calculatedOrder.setCommStatus(OrderCommStatus.SEND_REQ_TO_PAYMENT_SUCCESS);
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                calculatedOrder.setCommStatus(OrderCommStatus.SEND_REQ_TO_PAYMENT_ERROR);
            }
            orderRepository.update(calculatedOrder);
        });
    }

    private Handler<Message<Object>> paymentDoReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            Order order = orderEventModelConverter.convert(orderEventModel);
            order = orderRepository.updateStatus(order);
            if(order.getStatus().equals(OrderStatus.PAYMENT_SUCCESS)){
                scheduleDelivery(order);
                sendNotification(order, NotificationStatusEventModel.EVERYTHING_GOOD);
            }
            if(order.getStatus().equals(OrderStatus.PAYMENT_ERROR)){
                rollbackReservation(order);
                sendNotification(order, NotificationStatusEventModel.ALL_BAD);
            }
        };
    }

    private void sendNotification(Order order, NotificationStatusEventModel status) {
        eventBus.send(
                "notification.billing",
                SerializationUtils.serialize(new NotificationEventModel(order.getId(), status))
        );
    }

    private void scheduleDelivery(Order order) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        eventBus.request("schedule_delivery", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_DELIVERY_SUCCESS);
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_DELIVERY_ERROR);
            }
            orderRepository.updateStatus(order);
        });
    }

    private Handler<Message<Object>> scheduleDeliveryReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            Order order = orderEventModelConverter.convert(orderEventModel);
            order = orderRepository.updateStatus(order);
            if(order.getStatus().equals(OrderStatus.DELIVERY_SUCCESS)){
                // TODO: complete saga
                LOGGER.info("COMPLETE SAGA FOR -> {}", order);
            }
            if(order.getStatus().equals(OrderStatus.DELIVERY_ERROR)){
                rollbackPayment(order);
                rollbackReservation(order);
            }
        };
    }

    private void rollbackPayment(Order order) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        eventBus.request("payment_do_rollback", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_PAYMENT_ROLLBACK_SUCCESS);
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_PAYMENT_ROLLBACK_ERROR);
            }
            orderRepository.updateStatus(order);
        });
    }

    private void rollbackReservation(Order order) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        eventBus.request("storage_reserve_rollback", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_STORAGE_ROLLBACK_SUCCESS);
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_STORAGE_ROLLBACK_ERROR);
            }
            orderRepository.updateStatus(order);
        });
    }

    private Handler<Message<Object>> paymentDoRollbackReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            Order order = orderEventModelConverter.convert(orderEventModel);
            order = orderRepository.updateStatus(order);
            if(order.getStatus().equals(OrderStatus.PAYMENT_ROLLBACK)){
                LOGGER.info("PAYMENT ROLLBACK FOR -> {}", order);
                sendNotification(order, NotificationStatusEventModel.ALL_BAD);
            }
        };
    }

    private Handler<Message<Object>> storageReserveRollbackReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            Order order = orderEventModelConverter.convert(orderEventModel);
            order = orderRepository.updateStatus(order);
            if (order.getStatus().equals(OrderStatus.STORAGE_RESERVE_ROLLBACK)) {
                LOGGER.info("ITEMS RESERVATION ROLLBACK FOR -> {}", order);
            }
        };
    }

}

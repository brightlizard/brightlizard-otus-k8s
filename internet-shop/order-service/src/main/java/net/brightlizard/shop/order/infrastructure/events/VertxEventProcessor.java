package net.brightlizard.shop.order.infrastructure.events;

import io.vertx.core.Vertx;
import net.brightlizard.shop.event.model.notification.NotificationEventModel;
import net.brightlizard.shop.event.model.notification.NotificationStatusEventModel;
import net.brightlizard.shop.event.model.order.OrderCommStatusEventModel;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.order.core.converter.OrderEventModelConverter;
import net.brightlizard.shop.order.core.events.EventProcessor;
import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.OrderCommStatus;
import net.brightlizard.shop.order.core.saga.OrderSaga;
import net.brightlizard.shop.order.infrastructure.vertx.VertxContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import java.util.function.Consumer;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Service
@Lazy
public class VertxEventProcessor implements EventProcessor {

    private Logger LOGGER = LoggerFactory.getLogger(VertxEventProcessor.class);

    private Vertx vertx;
    private OrderEventModelConverter orderEventModelConverter;
    private OrderSaga orderSaga;

    public VertxEventProcessor(
        VertxContainer vertxContainer,
        OrderEventModelConverter orderEventModelConverter,
        @Lazy OrderSaga orderSaga
    ) {
        this.vertx = vertxContainer.getVertx();
        this.orderEventModelConverter = orderEventModelConverter;
        this.orderSaga = orderSaga;
    }

    @Override
    public void runStorageReserve(Order order, Consumer<Order> replyHandler) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        vertx.eventBus().request("run_storage_reserve", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                OrderEventModel oem = (OrderEventModel) SerializationUtils.deserialize((byte[]) ar.result().body());
                oem.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_STORAGE_SUCCESS);
                replyHandler.accept(orderEventModelConverter.convert(oem));
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_STORAGE_ERROR);
                replyHandler.accept(order);
            }
        });
    }

    @Override
    public void handleStorageReserveReply(OrderEventModel orderEventModel) {
        Order order = orderEventModelConverter.convert(orderEventModel);
        LOGGER.info("OBJECT FROM STORAGE RESERVE REPLY -> {}", orderEventModel);
        orderSaga.handleStorageReserveReply(order);
    }

    @Override
    public void runPaymentDo(Order order, Consumer<Order> replyHandler) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        vertx.eventBus().request("run_payment_do", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                OrderEventModel oem = (OrderEventModel) SerializationUtils.deserialize((byte[]) ar.result().body());
                oem.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_PAYMENT_SUCCESS);
                replyHandler.accept(orderEventModelConverter.convert(oem));
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_PAYMENT_ERROR);
                replyHandler.accept(order);
            }
        });
    }

    @Override
    public void handlePaymentDoReply(OrderEventModel orderEventModel) {
        Order order = orderEventModelConverter.convert(orderEventModel);
        LOGGER.info("OBJECT FROM PAYMENT DO REPLY -> {}", orderEventModel);
        orderSaga.handlePaymentDoReply(order);
    }

    @Override
    public void runScheduleDelivery(Order order, Consumer<Order> replyHandler) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        vertx.eventBus().request("run_schedule_delivery", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                OrderEventModel oem = (OrderEventModel) SerializationUtils.deserialize((byte[]) ar.result().body());
                oem.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_DELIVERY_SUCCESS);
                replyHandler.accept(orderEventModelConverter.convert(oem));
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_DELIVERY_ERROR);
                replyHandler.accept(order);
            }
        });
    }

    @Override
    public void handleScheduleDeliveryReply(OrderEventModel orderEventModel) {
        Order order = orderEventModelConverter.convert(orderEventModel);
        LOGGER.info("OBJECT FROM SCHEDULE DELIVERY REPLY -> {}", orderEventModel);
        orderSaga.handleScheduleDeliveryReply(order);
    }

    @Override
    public void runSendNotification(Order order, NotificationStatusEventModel status) {
        vertx.eventBus().send("run_notification", SerializationUtils.serialize(new NotificationEventModel(order.getId(), status)));
    }

    @Override
    public void runRollbackPayment(Order order, Consumer<Order> replyHandler) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        vertx.eventBus().request("run_rollback_payment", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                OrderEventModel oem = (OrderEventModel) SerializationUtils.deserialize((byte[]) ar.result().body());
                oem.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_PAYMENT_ROLLBACK_SUCCESS);
                replyHandler.accept(orderEventModelConverter.convert(oem));
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_PAYMENT_ROLLBACK_ERROR);
                replyHandler.accept(order);
            }
        });
    }

    @Override
    public void handlePaymentRollbackReply(OrderEventModel orderEventModel) {
        Order order = orderEventModelConverter.convert(orderEventModel);
        orderSaga.handlePaymentRollbackReply(order);
    }

    @Override
    public void handleReservationRollbackReply(OrderEventModel orderEventModel) {
        Order order = orderEventModelConverter.convert(orderEventModel);
        orderSaga.handleReservationRollbackReply(order);
    }

    @Override
    public void runRollbackReservation(Order order, Consumer<Order> replyHandler) {
        OrderEventModel orderEventModel = orderEventModelConverter.convert(order);
        vertx.eventBus().request("run_rollback_reservation", SerializationUtils.serialize(orderEventModel), ar -> {
            if(ar.succeeded()){
                LOGGER.info("ar.succeeded()");
                OrderEventModel oem = (OrderEventModel) SerializationUtils.deserialize((byte[]) ar.result().body());
                oem.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_STORAGE_ROLLBACK_SUCCESS);
                replyHandler.accept(orderEventModelConverter.convert(oem));
            }
            if(ar.failed()){
                LOGGER.info("ar.failed()");
                order.setCommStatus(OrderCommStatus.SEND_REQ_TO_STORAGE_ROLLBACK_ERROR);
                replyHandler.accept(order);
            }
        });
    }

}

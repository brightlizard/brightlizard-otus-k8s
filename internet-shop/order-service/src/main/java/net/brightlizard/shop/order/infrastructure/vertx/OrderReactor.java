package net.brightlizard.shop.order.infrastructure.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.event.model.notification.NotificationEventModel;
import net.brightlizard.shop.event.model.order.OrderCommStatusEventModel;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.order.core.converter.OrderEventModelConverter;
import net.brightlizard.shop.order.core.events.EventProcessor;
import net.brightlizard.shop.order.core.repository.OrderRepository;
import net.brightlizard.shop.order.core.service.OrderService;
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
    private EventProcessor eventProcessor;
    private EventBus eventBus;
    private OrderService orderService;
    private OrderRepository orderRepository;

    public OrderReactor(
        VertxContainer vertxContainer,
        OrderService orderService,
        OrderRepository orderRepository,
        OrderEventModelConverter orderEventModelConverter,
        EventProcessor eventProcessor
    ) {
        this.vertx = vertxContainer.getVertx();
        this.orderEventModelConverter = orderEventModelConverter;
        this.eventProcessor = eventProcessor;
        this.eventBus = vertx.eventBus();
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("run_storage_reserve", runStorageReserveHandler());
        eventBus.consumer("storage_reserve_reply", storageReserveReplyHandler());
        eventBus.consumer("run_payment_do", runPaymentDoHandler());
        eventBus.consumer("payment_do_reply", paymentDoReplyHandler());
        eventBus.consumer("run_schedule_delivery", runScheduleDeliveryHandler());
        eventBus.consumer("schedule_delivery_reply", scheduleDeliveryReplyHandler());
        eventBus.consumer("run_rollback_payment", runRollbackPaymentHandler());
        eventBus.consumer("payment_do_rollback_reply", paymentDoRollbackReplyHandler());
        eventBus.consumer("run_rollback_reservation", runRollbackReservationHandler());
        eventBus.consumer("storage_reserve_rollback_reply", storageReserveRollbackReplyHandler());
        eventBus.consumer("run_notification", sendNotification());

        startPromise.complete();
    }

    private Handler<Message<Object>> runStorageReserveHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            LOGGER.info("OBJECT TO STORAGE -> {}", orderEventModel);
            eventBus.request("storage_reserve", SerializationUtils.serialize(orderEventModel), ar -> {
                if(ar.succeeded()){
                    LOGGER.info("ar.succeeded()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_STORAGE_SUCCESS);
                }
                if(ar.failed()){
                    LOGGER.info("ar.failed()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_STORAGE_ERROR);
                }
                message.reply(SerializationUtils.serialize(orderEventModel));
            });
        };
    }

    private Handler<Message<Object>> storageReserveReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            LOGGER.info("OBJECT FROM STORAGE -> {}", orderEventModel);
            eventProcessor.handleStorageReserveReply(orderEventModel);
        };
    }

    private Handler<Message<Object>> runPaymentDoHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            LOGGER.info("OBJECT TO PAYMENT -> {}", orderEventModel);
            eventBus.request("payment_do", SerializationUtils.serialize(orderEventModel), ar -> {
                if(ar.succeeded()){
                    LOGGER.info("ar.succeeded()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_PAYMENT_SUCCESS);
                }
                if(ar.failed()){
                    LOGGER.info("ar.failed()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_PAYMENT_ERROR);
                }
                message.reply(SerializationUtils.serialize(orderEventModel));
            });
        };
    }

    private Handler<Message<Object>> paymentDoReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            LOGGER.info("OBJECT FROM PAYMENT -> {}", orderEventModel);
            eventProcessor.handlePaymentDoReply(orderEventModel);
        };
    }

    private Handler<Message<Object>> sendNotification() {
        return message -> {
            NotificationEventModel notificationEventModel = (NotificationEventModel) SerializationUtils.deserialize((byte[]) message.body());
            eventBus.send(
                "notification.billing",
                SerializationUtils.serialize(notificationEventModel)
            );
        };
    }

    private Handler<Message<Object>> runScheduleDeliveryHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            LOGGER.info("OBJECT TO DELIVERY -> {}", orderEventModel);
            eventBus.request("schedule_delivery", SerializationUtils.serialize(orderEventModel), ar -> {
                if(ar.succeeded()){
                    LOGGER.info("ar.succeeded()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_DELIVERY_SUCCESS);
                }
                if(ar.failed()){
                    LOGGER.info("ar.failed()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_DELIVERY_ERROR);
                }
                message.reply(SerializationUtils.serialize(orderEventModel));
            });
        };
    }

    private Handler<Message<Object>> scheduleDeliveryReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            LOGGER.info("OBJECT FROM DELIVERY -> {}", orderEventModel);
            eventProcessor.handleScheduleDeliveryReply(orderEventModel);
        };
    }

    private Handler<Message<Object>> runRollbackPaymentHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            eventBus.request("payment_do_rollback", SerializationUtils.serialize(orderEventModel), ar -> {
                if(ar.succeeded()){
                    LOGGER.info("ar.succeeded()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_PAYMENT_ROLLBACK_SUCCESS);
                }
                if(ar.failed()){
                    LOGGER.info("ar.failed()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_PAYMENT_ROLLBACK_ERROR);
                }
                message.reply(SerializationUtils.serialize(orderEventModel));
            });
        };
    }

    private Handler<Message<Object>> runRollbackReservationHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            eventBus.request("storage_reserve_rollback", SerializationUtils.serialize(orderEventModel), ar -> {
                if(ar.succeeded()){
                    LOGGER.info("ar.succeeded()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_STORAGE_ROLLBACK_SUCCESS);
                }
                if(ar.failed()){
                    LOGGER.info("ar.failed()");
                    orderEventModel.setCommStatus(OrderCommStatusEventModel.SEND_REQ_TO_STORAGE_ROLLBACK_ERROR);
                }
                message.reply(SerializationUtils.serialize(orderEventModel));
            });
        };
    }

    private Handler<Message<Object>> paymentDoRollbackReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            eventProcessor.handlePaymentRollbackReply(orderEventModel);
        };
    }

    private Handler<Message<Object>> storageReserveRollbackReplyHandler() {
        return message -> {
            OrderEventModel orderEventModel = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            eventProcessor.handleReservationRollbackReply(orderEventModel);
        };
    }

}

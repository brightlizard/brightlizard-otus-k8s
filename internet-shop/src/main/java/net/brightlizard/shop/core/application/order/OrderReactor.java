package net.brightlizard.shop.core.application.order;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderCommStatus;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.repository.OrderRepository;
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
    private EventBus eventBus;
    private OrderService orderService;
    private OrderRepository orderRepository;

    public OrderReactor(Vertx vertx, OrderService orderService, OrderRepository orderRepository) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("order_created", orderCreatedHandler());
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
            eventBus.request("storage_reserve", SerializationUtils.serialize(order), ar -> {
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
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            order = orderRepository.updateStatusAndItems(order);
            if(order.getStatus().equals(OrderStatus.STORAGE_RESERVE_SUCCESS)){
                doPaymentRequest(order);
            }
        };
    }

    private void doPaymentRequest(Order order) {
        Order calculatedOrder = orderService.calculateTotalSum(order);
        eventBus.request("payment_do", SerializationUtils.serialize(order), ar -> {
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
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            order = orderRepository.updateStatus(order);
            if(order.getStatus().equals(OrderStatus.PAYMENT_SUCCESS)){
                scheduleDelivery(order);
            }
            if(order.getStatus().equals(OrderStatus.PAYMENT_ERROR)){
                rollbackReservation(order);
            }
        };
    }

    private void scheduleDelivery(Order order) {
        eventBus.request("schedule_delivery", SerializationUtils.serialize(order), ar -> {
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
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
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
        eventBus.request("payment_do_rollback", SerializationUtils.serialize(order), ar -> {
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
        eventBus.request("storage_reserve_rollback", SerializationUtils.serialize(order), ar -> {
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
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            order = orderRepository.updateStatus(order);
            if(order.getStatus().equals(OrderStatus.PAYMENT_ROLLBACK)){
                LOGGER.info("PAYMENT ROLLBACK FOR -> {}", order);
            }
        };
    }

    private Handler<Message<Object>> storageReserveRollbackReplyHandler() {
        return message -> {
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            order = orderRepository.updateStatus(order);
            if (order.getStatus().equals(OrderStatus.STORAGE_RESERVE_ROLLBACK)) {
                LOGGER.info("ITEMS RESERVATION ROLLBACK FOR -> {}", order);
            }
        };
    }

}

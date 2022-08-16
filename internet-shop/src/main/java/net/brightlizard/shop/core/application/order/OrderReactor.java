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
                preparePaymentRequest(order);
            }
        };
    }

    private void preparePaymentRequest(Order order) {
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
                // delivery
            }
            if(order.getStatus().equals(OrderStatus.PAYMENT_ERROR)){
                // rollback items reservation
            }
        };
    }

}

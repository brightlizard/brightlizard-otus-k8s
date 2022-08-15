package net.brightlizard.shop.core.application.order;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.repository.OrderRepository;
import net.brightlizard.shop.core.application.storage.model.ReservationResult;
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


        startPromise.complete();
    }

    private Handler<Message<Object>> orderCreatedHandler() {
        return message -> {
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());

            eventBus.request("storage_reserve", SerializationUtils.serialize(order), ar -> {
                if(ar.succeeded()){
                    order.setStatus(OrderStatus.SEND_REQ_TO_STORAGE_SUCCESS);
                }
                if(ar.failed()){
                    order.setStatus(OrderStatus.SEND_REQ_TO_STORAGE_ERROR);
                }
                orderRepository.update(order);
            });

        };
    }

    private Handler<Message<Object>> storageReserveReplyHandler() {
        return message -> {
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            orderRepository.update(order);
        };
    }

}

package net.brightlizard.shop.core.application.storage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.storage.model.ReservationResult;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 *
 * Storage Actor
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StorageReactor extends AbstractVerticle {

    private Vertx vertx;
    private EventBus eventBus;
    private StorageService storageService;

    public StorageReactor(Vertx vertx, StorageService storageService) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.storageService = storageService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("storage_reserve", storageReserveHandler());

        startPromise.complete();
    }

    private Handler<Message<Object>> storageReserveHandler() {
        return message -> {
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            ReservationResult reservationResult = storageService.reserve(order);
            if(reservationResult.equals(ReservationResult.SUCCESS)){
                order.setStatus(OrderStatus.STORAGE_RESERVE_SUCCESS);
            }
            if(reservationResult.equals(ReservationResult.ERROR)){
                order.setStatus(OrderStatus.STORAGE_RESERVE_ERROR);
            }
            eventBus.send("storage_reserve_reply", SerializationUtils.serialize(order));
        };
    }
}

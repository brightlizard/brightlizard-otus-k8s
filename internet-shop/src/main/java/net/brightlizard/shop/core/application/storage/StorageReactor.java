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
        eventBus.consumer("storage_reserve_rollback", storageReserveRollbackHandler());

        startPromise.complete();
    }

    private Handler<Message<Object>> storageReserveHandler() {
        return message -> {
            message.reply("success");
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            order = storageService.reserve(order);
            eventBus.send("storage_reserve_reply", SerializationUtils.serialize(order));
        };
    }

    private Handler<Message<Object>> storageReserveRollbackHandler() {
        return message -> {
            message.reply("success");
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            order = storageService.rollback(order);
            eventBus.send("storage_reserve_rollback_reply", SerializationUtils.serialize(order));
        };
    }

}

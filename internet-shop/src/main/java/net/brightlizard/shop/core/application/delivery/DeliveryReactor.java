package net.brightlizard.shop.core.application.delivery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.infrastructure.vertx.VertxContainer;
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
 * Delivery Actor
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeliveryReactor extends AbstractVerticle {

    private Logger LOGGER = LoggerFactory.getLogger(DeliveryReactor.class);

    private Vertx vertx;
    private EventBus eventBus;
    private DeliveryService deliveryService;

    public DeliveryReactor(VertxContainer vertxContainer, DeliveryService deliveryService) {
        this.vertx = vertxContainer.getVertx();
        this.eventBus = vertx.eventBus();
        this.deliveryService = deliveryService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("schedule_delivery", scheduleDeliveryHandler());

        startPromise.complete();
    }

    private Handler<Message<Object>> scheduleDeliveryHandler() {
        return message -> {
            message.reply("success");
            OrderEventModel order = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            OrderEventModel scheduledOrder = deliveryService.schedule(order);
            eventBus.send("schedule_delivery_reply", SerializationUtils.serialize(scheduledOrder));
        };
    }
}

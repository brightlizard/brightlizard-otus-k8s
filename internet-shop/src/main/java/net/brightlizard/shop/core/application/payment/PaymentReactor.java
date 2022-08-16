package net.brightlizard.shop.core.application.payment;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.core.application.order.OrderServiceImpl;
import net.brightlizard.shop.core.application.order.model.Order;
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
 * Storage Actor
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PaymentReactor extends AbstractVerticle {

    private Logger LOGGER = LoggerFactory.getLogger(PaymentReactor.class);

    private Vertx vertx;
    private EventBus eventBus;
    private PaymentService paymentService;

    public PaymentReactor(Vertx vertx, PaymentService paymentService) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.paymentService = paymentService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("payment_do", paymentDoHandler());

        startPromise.complete();
    }

    private Handler<Message<Object>> paymentDoHandler() {
        return message -> {
            Order order = (Order) SerializationUtils.deserialize((byte[]) message.body());
            Order processedOrder = paymentService.process(order);
            eventBus.send("payment_do_reply", SerializationUtils.serialize(processedOrder));
        };
    }
}

package net.brightlizard.shop.payment.infrastructure.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.payment.core.service.PaymentService;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.configuration.VertxContainer;
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
 * Payment Actor
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PaymentReactor extends AbstractVerticle {

    private Logger LOGGER = LoggerFactory.getLogger(PaymentReactor.class);

    private Vertx vertx;
    private EventBus eventBus;
    private PaymentService paymentService;

    public PaymentReactor(VertxContainer vertxContainer, PaymentService paymentService) {
        this.vertx = vertxContainer.getVertx();
        this.eventBus = vertx.eventBus();
        this.paymentService = paymentService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        eventBus.consumer("payment_do", paymentDoHandler());
        eventBus.consumer("payment_do_rollback", paymentDoRollbackHandler());

        startPromise.complete();
    }

    private Handler<Message<Object>> paymentDoHandler() {
        return message -> {
            message.reply("success");
            OrderEventModel order = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            paymentService.process(order);
        };
    }

    private Handler<Message<Object>> paymentDoRollbackHandler() {
        return message -> {
            message.reply("success");
            OrderEventModel order = (OrderEventModel) SerializationUtils.deserialize((byte[]) message.body());
            paymentService.rollback(order);
        };
    }
}

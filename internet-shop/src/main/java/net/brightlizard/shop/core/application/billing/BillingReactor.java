package net.brightlizard.shop.core.application.billing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.core.application.billing.model.*;
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
public class BillingReactor extends AbstractVerticle {

    private Logger LOGGER = LoggerFactory.getLogger(BillingReactor.class);

    private Vertx vertx;
    private EventBus eventBus;
    private BillingService billingService;

    public BillingReactor(Vertx vertx, BillingService billingService) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.billingService = billingService;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        eventBus.consumer("billing.withdraw", withdraw());
        eventBus.consumer("billing.deposit", deposit());
        startPromise.complete();
    }

    private Handler<Message<Object>> withdraw() {
        return message -> {
            LOGGER.info("WITHDRAW ENTER");
            try {
                WithdrawRequest withdrawRequest = (WithdrawRequest) SerializationUtils.deserialize((byte[]) message.body());
                WithdrawStatus withdrawStatus = billingService.withdraw(withdrawRequest);
                message.reply(SerializationUtils.serialize(new WithdrawResponse(withdrawStatus)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private Handler<Message<Object>> deposit() {
        return message -> {
            DepositRequest depositRequest = (DepositRequest) SerializationUtils.deserialize((byte[]) message.body());
            DepositStatus depositStatus = billingService.deposit(depositRequest);
            message.reply(SerializationUtils.serialize(depositStatus));
        };
    }
}

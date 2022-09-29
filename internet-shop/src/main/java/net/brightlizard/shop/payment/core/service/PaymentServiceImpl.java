package net.brightlizard.shop.payment.core.service;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import net.brightlizard.shop.billing.core.model.*;
import net.brightlizard.shop.billing.core.repository.CustomerAccountRepository;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.event.model.order.OrderStatusEventModel;
import net.brightlizard.shop.configuration.VertxContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private Vertx vertx;
    private EventBus eventBus;
    private CustomerAccountRepository customerAccountRepository;

    public PaymentServiceImpl(VertxContainer vertxContainer, CustomerAccountRepository customerAccountRepository) {
        this.vertx = vertxContainer.getVertx();
        this.eventBus = vertx.eventBus();
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    public void process(OrderEventModel order) {
        WithdrawRequest withdrawRequest = new WithdrawRequest(order.getCustomer(), order.getTotalPrice());
        // Какая-то дополнительная логика применимая к платежу
        eventBus.request("billing.withdraw", SerializationUtils.serialize(withdrawRequest), ar -> {
            if(ar.succeeded()){
                LOGGER.info("BILLING WITHDRAW SUCCESS REPLY");
                WithdrawResponse withdrawResponse = (WithdrawResponse) SerializationUtils.deserialize((byte[]) ar.result().body());
                if(withdrawResponse.getStatus().equals(WithdrawStatus.SUCCESS)){
                    order.setStatus(OrderStatusEventModel.PAYMENT_SUCCESS);
                }
                if(withdrawResponse.getStatus().equals(WithdrawStatus.FAIL)){
                    order.setStatus(OrderStatusEventModel.PAYMENT_ERROR);
                }
            }
            if(ar.failed()){
                LOGGER.info("BILLING WITHDRAW FAILED");
                order.setStatus(OrderStatusEventModel.PAYMENT_ERROR);
            }
            eventBus.send("payment_do_reply", SerializationUtils.serialize(order));
        });
    }

    @Override
    public void rollback(OrderEventModel order) {
        DepositRequest depositRequest = new DepositRequest(order.getCustomer(), order.getTotalPrice());
        // Какая-то дополнительная логика применимая к откату платежа
        eventBus.request("billing.deposit", SerializationUtils.serialize(depositRequest), ar -> {
            DepositResponse depositResponse = (DepositResponse) SerializationUtils.deserialize((byte[]) ar.result().body());
            if(ar.succeeded()){
                if(depositResponse.getStatus().equals(DepositStatus.SUCCESS)){
                    order.setStatus(OrderStatusEventModel.PAYMENT_ROLLBACK);
                }
                if(depositResponse.getStatus().equals(DepositStatus.FAIL)){
                    order.setStatus(OrderStatusEventModel.PAYMENT_ERROR);
                }
            }
            if(ar.failed()){
                order.setStatus(OrderStatusEventModel.PAYMENT_ERROR);
            }
            eventBus.send("payment_do_rollback_reply", SerializationUtils.serialize(order));
        });
    }

}

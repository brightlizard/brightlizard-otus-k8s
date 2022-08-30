package net.brightlizard.shop.core.application.payment;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import net.brightlizard.shop.core.application.billing.model.*;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.billing.repository.CustomerAccountRepository;
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

    public PaymentServiceImpl(Vertx vertx, CustomerAccountRepository customerAccountRepository) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    public void process(Order order) {
        WithdrawRequest withdrawRequest = new WithdrawRequest(order.getCustomer(), order.getTotalPrice());
        // Какая-то дополнительная логика применимая к платежу
        eventBus.request("billing.withdraw", SerializationUtils.serialize(withdrawRequest), ar -> {
            if(ar.succeeded()){
                LOGGER.info("BILLING WITHDRAW SUCCESS REPLY");
                WithdrawResponse withdrawResponse = (WithdrawResponse) SerializationUtils.deserialize((byte[]) ar.result().body());
                if(withdrawResponse.getStatus().equals(WithdrawStatus.SUCCESS)){
                    order.setStatus(OrderStatus.PAYMENT_SUCCESS);
                }
                if(withdrawResponse.getStatus().equals(WithdrawStatus.FAIL)){
                    order.setStatus(OrderStatus.PAYMENT_ERROR);
                }
            }
            if(ar.failed()){
                LOGGER.info("BILLING WITHDRAW FAILED");
                order.setStatus(OrderStatus.PAYMENT_ERROR);
            }
            eventBus.send("payment_do_reply", SerializationUtils.serialize(order));
        });
    }

    @Override
    public void rollback(Order order) {
        DepositRequest depositRequest = new DepositRequest(order.getCustomer(), order.getTotalPrice());
        // Какая-то дополнительная логика применимая к откату платежа
        eventBus.request("billing.deposit", SerializationUtils.serialize(depositRequest), ar -> {
            DepositResponse depositResponse = (DepositResponse) SerializationUtils.deserialize((byte[]) ar.result().body());
            if(ar.succeeded()){
                if(depositResponse.getStatus().equals(DepositStatus.SUCCESS)){
                    order.setStatus(OrderStatus.PAYMENT_ROLLBACK);
                }
                if(depositResponse.getStatus().equals(DepositStatus.FAIL)){
                    order.setStatus(OrderStatus.PAYMENT_ERROR);
                }
            }
            if(ar.failed()){
                order.setStatus(OrderStatus.PAYMENT_ERROR);
            }
            eventBus.send("payment_do_rollback_reply", SerializationUtils.serialize(order));
        });
    }

}

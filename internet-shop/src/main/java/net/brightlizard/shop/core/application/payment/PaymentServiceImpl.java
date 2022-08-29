package net.brightlizard.shop.core.application.payment;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import net.brightlizard.shop.core.application.billing.model.WithdrawResponse;
import net.brightlizard.shop.core.application.billing.model.WithdrawStatus;
import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.repository.CustomerAccountRepository;
import net.brightlizard.shop.core.application.payment.model.WithdrawRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private Vertx vertx;
    private EventBus eventBus;
    private CustomerAccountRepository customerAccountRepository;

    public PaymentServiceImpl(Vertx vertx, CustomerAccountRepository customerAccountRepository) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    public Order process(Order order) {
        WithdrawRequest withdrawRequest = new WithdrawRequest(order.getConsumer(), order.getTotalPrice());
        eventBus.request("withdraw", withdrawRequest, ar -> {
            WithdrawResponse withdrawResponse = (WithdrawResponse) SerializationUtils.deserialize((byte[]) ar.result().body());
            if(ar.succeeded()){
                if(withdrawResponse.getStatus().equals(WithdrawStatus.SUCCESS)){
                    order.setStatus(OrderStatus.PAYMENT_SUCCESS);
                }
                if(withdrawResponse.getStatus().equals(WithdrawStatus.FAIL)){
                    order.setStatus(OrderStatus.PAYMENT_ERROR);
                }
            }
            if(ar.failed()){
                order.setStatus(OrderStatus.PAYMENT_ERROR);
            }
            eventBus.send("payment_do_reply", SerializationUtils.serialize(order));
        });
        return order;
    }

    @Override
    public Order rollback(Order order) {
        CustomerAccount customerAccount = customerAccountRepository.findById(order.getConsumer());
        double balance = customerAccount.getBalance();
        double newBalance = balance + order.getTotalPrice();
        customerAccount.setBalance(newBalance);
        customerAccountRepository.update(customerAccount);
        order.setStatus(OrderStatus.PAYMENT_ROLLBACK);
        return order;
    }

}

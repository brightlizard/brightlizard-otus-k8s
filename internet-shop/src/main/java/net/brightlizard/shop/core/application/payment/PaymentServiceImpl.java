package net.brightlizard.shop.core.application.payment;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.payment.model.CustomerAccount;
import net.brightlizard.shop.core.application.payment.repository.CustomerAccountRepository;
import org.springframework.stereotype.Service;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private CustomerAccountRepository customerAccountRepository;

    public PaymentServiceImpl(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }


    @Override
    public Order process(Order order) {
        CustomerAccount customerAccount = customerAccountRepository.findById(order.getConsumer());
        double balance = customerAccount.getBalance();
        double restBalance = balance - order.getTotalPrice();
        if(restBalance < 0) {
            order.setStatus(OrderStatus.PAYMENT_ERROR);
            return order;
        }
        customerAccount.setBalance(restBalance);
        order.setStatus(OrderStatus.PAYMENT_SUCCESS);
        return order;
    }
}

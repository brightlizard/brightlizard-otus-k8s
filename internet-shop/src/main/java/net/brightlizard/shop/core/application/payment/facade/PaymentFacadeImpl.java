package net.brightlizard.shop.core.application.payment.facade;

import net.brightlizard.shop.core.application.payment.model.CustomerAccount;
import net.brightlizard.shop.core.application.payment.repository.CustomerAccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class PaymentFacadeImpl implements PaymentFacade {

    private CustomerAccountRepository customerAccountRepository;

    public PaymentFacadeImpl(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    public List<CustomerAccount> getCustomerAccounts() {
        return customerAccountRepository.findAll();
    }

}

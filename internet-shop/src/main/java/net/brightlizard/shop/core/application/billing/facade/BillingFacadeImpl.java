package net.brightlizard.shop.core.application.billing.facade;

import net.brightlizard.shop.core.application.billing.model.Customer;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.repository.CustomerAccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class BillingFacadeImpl implements BillingFacade {

    private CustomerAccountRepository customerAccountRepository;

    public BillingFacadeImpl(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    public List<CustomerAccount> getCustomerAccounts() {
        return customerAccountRepository.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return null;
    }

}

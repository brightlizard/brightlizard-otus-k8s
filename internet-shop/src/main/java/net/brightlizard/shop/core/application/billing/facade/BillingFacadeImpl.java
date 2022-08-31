package net.brightlizard.shop.core.application.billing.facade;

import net.brightlizard.shop.core.application.billing.BillingService;
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
    private BillingService billingService;

    public BillingFacadeImpl(CustomerAccountRepository customerAccountRepository, BillingService billingService) {
        this.customerAccountRepository = customerAccountRepository;
        this.billingService = billingService;
    }

    @Override
    public List<Customer> getCustomers() {
        return customerAccountRepository.findAllCustomers();
    }

    @Override
    public List<CustomerAccount> getCustomerAccounts() {
        return customerAccountRepository.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return billingService.createCustomer(customer);
    }

}

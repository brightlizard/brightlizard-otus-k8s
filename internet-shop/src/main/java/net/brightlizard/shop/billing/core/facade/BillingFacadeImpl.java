package net.brightlizard.shop.billing.core.facade;

import net.brightlizard.shop.billing.core.service.BillingService;
import net.brightlizard.shop.billing.core.model.Customer;
import net.brightlizard.shop.billing.core.model.CustomerAccount;
import net.brightlizard.shop.billing.core.model.DepositRequest;
import net.brightlizard.shop.billing.core.model.DepositResponse;
import net.brightlizard.shop.billing.core.repository.CustomerAccountRepository;
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

    @Override
    public Customer getCustomer(String customerId) {
        return billingService.getCustomer(customerId);
    }

    @Override
    public CustomerAccount getCustomerAccount(String customerId) {
        return billingService.getCustomerAccount(customerId);
    }

    @Override
    public DepositResponse deposit(DepositRequest depositRequest) {
        return new DepositResponse(billingService.deposit(depositRequest));
    }

}

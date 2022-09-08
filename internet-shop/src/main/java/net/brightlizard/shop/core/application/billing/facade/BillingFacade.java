package net.brightlizard.shop.core.application.billing.facade;

import net.brightlizard.shop.core.application.billing.model.Customer;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.model.DepositRequest;
import net.brightlizard.shop.core.application.billing.model.DepositResponse;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface BillingFacade {
    List<Customer> getCustomers();
    List<CustomerAccount> getCustomerAccounts();
    Customer createCustomer(Customer customer);
    Customer getCustomer(String customerId);
    CustomerAccount getCustomerAccount(String customerId);
    DepositResponse deposit(DepositRequest depositRequest);
}

package net.brightlizard.shop.billing.core.facade;

import net.brightlizard.shop.billing.core.model.Customer;
import net.brightlizard.shop.billing.core.model.CustomerAccount;
import net.brightlizard.shop.billing.core.model.DepositRequest;
import net.brightlizard.shop.billing.core.model.DepositResponse;

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

package net.brightlizard.shop.core.application.billing.facade;

import net.brightlizard.shop.core.application.billing.model.Customer;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface BillingFacade {

    List<CustomerAccount> getCustomerAccounts();
    Customer createCustomer(Customer customer);

}

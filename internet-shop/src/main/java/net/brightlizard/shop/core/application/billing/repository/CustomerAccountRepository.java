package net.brightlizard.shop.core.application.billing.repository;

import net.brightlizard.shop.core.application.billing.model.Customer;
import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface CustomerAccountRepository /*extends Repository<CustomerAccount, String>*/ {

    Customer createCustomerAndAccount(Customer customer);
    CustomerAccount findById(String id);
    List<CustomerAccount> findAll();
    CustomerAccount update(CustomerAccount customerAccount);
    List<Customer> findAllCustomers();
}

package net.brightlizard.shop.billing.core.repository;

import net.brightlizard.shop.billing.core.model.Customer;
import net.brightlizard.shop.billing.core.model.CustomerAccount;
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
    Customer findCustomerById(String id);

}

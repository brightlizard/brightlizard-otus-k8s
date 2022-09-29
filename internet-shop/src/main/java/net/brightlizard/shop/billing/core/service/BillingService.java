package net.brightlizard.shop.billing.core.service;

import net.brightlizard.shop.billing.core.model.*;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface BillingService {

    Customer createCustomer(Customer customer);
    WithdrawStatus withdraw(WithdrawRequest withdrawRequest);
    DepositStatus deposit(DepositRequest depositRequest);
    Customer getCustomer(String customerId);
    CustomerAccount getCustomerAccount(String customerId);
}

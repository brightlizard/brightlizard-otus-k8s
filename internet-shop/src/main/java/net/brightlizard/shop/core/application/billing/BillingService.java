package net.brightlizard.shop.core.application.billing;

import net.brightlizard.shop.core.application.billing.model.*;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface BillingService {

    Customer createCustomer(Customer customer);
    WithdrawStatus withdraw(WithdrawRequest withdrawRequest);
    DepositStatus deposit(DepositRequest depositRequest);

}

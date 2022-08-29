package net.brightlizard.shop.core.application.billing;

import net.brightlizard.shop.core.application.billing.model.DepositStatus;
import net.brightlizard.shop.core.application.billing.model.WithdrawStatus;
import net.brightlizard.shop.core.application.billing.model.DepositRequest;
import net.brightlizard.shop.core.application.billing.model.WithdrawRequest;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface BillingService {

    WithdrawStatus withdraw(WithdrawRequest withdrawRequest);
    DepositStatus deposit(DepositRequest depositRequest);


}

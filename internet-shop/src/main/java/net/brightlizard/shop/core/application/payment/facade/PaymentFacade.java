package net.brightlizard.shop.core.application.payment.facade;

import net.brightlizard.shop.core.application.payment.model.CustomerAccount;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface PaymentFacade {

    List<CustomerAccount> getCustomerAccounts();

}

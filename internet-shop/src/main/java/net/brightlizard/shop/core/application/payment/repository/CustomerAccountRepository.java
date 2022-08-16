package net.brightlizard.shop.core.application.payment.repository;

import net.brightlizard.shop.core.application.payment.model.CustomerAccount;
import org.springframework.data.repository.Repository;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface CustomerAccountRepository extends Repository<CustomerAccount, String> {

    CustomerAccount findById(String id);
    List<CustomerAccount> findAll();
    CustomerAccount update(CustomerAccount customerAccount);

}

package net.brightlizard.shop.core.application.billing.repository;

import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.model.CustomerAccountStatus;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class CustomerAccountRepositoryImpl implements CustomerAccountRepository {

    private HashMap<String, CustomerAccount> customerById = new HashMap<>(){{
        put("00000000-0000-0000-00000000", new CustomerAccount("00000000-0000-0000-00000000", 526000.00, CustomerAccountStatus.ACTIVE));
    }};


    @Override
    public CustomerAccount findById(String id) {
        return new CustomerAccount(customerById.get(id));
    }

    @Override
    public List<CustomerAccount> findAll() {
        return new ArrayList<>(customerById.values());
    }

    @Override
    public CustomerAccount update(CustomerAccount customerAccount) {
        return customerById.put(customerAccount.getCustomerId(), new CustomerAccount(customerAccount));
    }

}

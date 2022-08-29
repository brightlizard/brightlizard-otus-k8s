package net.brightlizard.shop.core.application.billing;

import net.brightlizard.shop.core.application.billing.model.CustomerAccount;
import net.brightlizard.shop.core.application.billing.model.DepositStatus;
import net.brightlizard.shop.core.application.billing.model.WithdrawStatus;
import net.brightlizard.shop.core.application.billing.repository.CustomerAccountRepository;
import net.brightlizard.shop.core.application.billing.model.DepositRequest;
import net.brightlizard.shop.core.application.billing.model.WithdrawRequest;
import org.springframework.stereotype.Service;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Service
public class BillingServiceImpl implements BillingService {

    private CustomerAccountRepository customerAccountRepository;

    public BillingServiceImpl(CustomerAccountRepository customerAccountRepository) {
        this.customerAccountRepository = customerAccountRepository;
    }

    @Override
    public WithdrawStatus withdraw(WithdrawRequest withdrawRequest) {
        try {
            CustomerAccount customerAccount = customerAccountRepository.findById(withdrawRequest.getCustomerId());
            double balance = customerAccount.getBalance();
            double restBalance = balance - withdrawRequest.getTotalSum();
            if(restBalance < 0) {
                return WithdrawStatus.FAIL;
            }
            customerAccount.setBalance(restBalance);
            customerAccountRepository.update(customerAccount);

            return WithdrawStatus.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return WithdrawStatus.FAIL;
        }
    }

    @Override
    public DepositStatus deposit(DepositRequest depositRequest) {
        try {
            CustomerAccount customerAccount = customerAccountRepository.findById(depositRequest.getCustomerId());
            double balance = customerAccount.getBalance();
            double newBalance = balance + depositRequest.getTotalSum();
            customerAccount.setBalance(newBalance);
            customerAccountRepository.update(customerAccount);
            return DepositStatus.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return DepositStatus.FAIL;
        }
    }

}

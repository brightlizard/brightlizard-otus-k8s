package net.brightlizard.shop.billing.core.service;

import net.brightlizard.shop.billing.core.model.*;
import net.brightlizard.shop.billing.core.repository.CustomerAccountRepository;
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
    public Customer createCustomer(Customer customer) {
        Customer customer1 = customerAccountRepository.createCustomerAndAccount(customer);
        return customer1;
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

    @Override
    public Customer getCustomer(String customerId) {
        return customerAccountRepository.findCustomerById(customerId);
    }

    @Override
    public CustomerAccount getCustomerAccount(String customerId) {
        return customerAccountRepository.findById(customerId);
    }

}

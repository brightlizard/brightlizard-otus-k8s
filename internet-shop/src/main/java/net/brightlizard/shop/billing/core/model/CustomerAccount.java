package net.brightlizard.shop.billing.core.model;

import java.util.Objects;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class CustomerAccount {

    private String id;
    private String customerId;
    private double balance;
    private CustomerAccountStatus status;

    public CustomerAccount(String id, String customerId, double balance, CustomerAccountStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.status = status;
    }

    public CustomerAccount(CustomerAccount customerAccount) {
        setCustomerId(customerAccount.getCustomerId());
        setBalance(customerAccount.getBalance());
        setStatus(customerAccount.getStatus());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public CustomerAccountStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerAccountStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAccount that = (CustomerAccount) o;
        return Double.compare(that.balance, balance) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(customerId, that.customerId) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, balance, status);
    }
}

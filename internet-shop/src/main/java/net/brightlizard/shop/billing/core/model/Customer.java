package net.brightlizard.shop.billing.core.model;

import java.util.Objects;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class Customer {

    private String id;
    private String name;
    private CustomerStatus status;
    private CustomerAccount account;

    public Customer(String name) {
        this.name = name;
        this.status = CustomerStatus.ACTIVE;
    }

    public Customer(String id, String name, CustomerStatus status, CustomerAccount account) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.account = account;
    }

    public Customer(String id, String name, CustomerStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public CustomerAccount getAccount() {
        return account;
    }

    public void setAccount(CustomerAccount account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) &&
                Objects.equals(name, customer.name) &&
                status == customer.status &&
                Objects.equals(account, customer.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, account);
    }
}

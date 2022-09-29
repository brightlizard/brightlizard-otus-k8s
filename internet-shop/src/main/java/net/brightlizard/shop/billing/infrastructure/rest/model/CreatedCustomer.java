package net.brightlizard.shop.billing.infrastructure.rest.model;

import net.brightlizard.shop.billing.core.model.CustomerAccount;
import net.brightlizard.shop.billing.core.model.CustomerStatus;


/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class CreatedCustomer {

    private String id;
    private String name;
    private CustomerStatus status;
    private CustomerAccount account;

    public CreatedCustomer() {
    }

    public CreatedCustomer(String id, String name, CustomerStatus status, CustomerAccount account) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.account = account;
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
}

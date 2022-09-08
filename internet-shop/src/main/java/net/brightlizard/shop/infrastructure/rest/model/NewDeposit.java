package net.brightlizard.shop.infrastructure.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class NewDeposit {

    @NotNull
    @JsonProperty(value = "deposit_value", required = true)
    private double depositValue;

    public double getDepositValue() {
        return depositValue;
    }

    public void setDepositValue(double depositValue) {
        this.depositValue = depositValue;
    }

}

package net.brightlizard.shop.core.application.billing.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class WithdrawRequest implements Serializable {

    private String customerId;
    private double totalSum;

    public WithdrawRequest(String customerId, double totalSum) {
        this.customerId = customerId;
        this.totalSum = totalSum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawRequest that = (WithdrawRequest) o;
        return Double.compare(that.totalSum, totalSum) == 0 &&
                Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, totalSum);
    }

    @Override
    public String toString() {
        return "WithdrawRequest{" +
                "customerId='" + customerId + '\'' +
                ", totalSum=" + totalSum +
                '}';
    }
}

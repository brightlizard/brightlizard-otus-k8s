package net.brightlizard.shop.core.application.billing.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class DepositResponse implements Serializable {

    private DepositStatus status;

    public DepositResponse(DepositStatus status) {
        this.status = status;
    }

    public DepositStatus getStatus() {
        return status;
    }

    public void setStatus(DepositStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositResponse that = (DepositResponse) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}

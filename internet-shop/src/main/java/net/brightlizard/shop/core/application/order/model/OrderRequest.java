package net.brightlizard.shop.core.application.order.model;

import java.util.Objects;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class OrderRequest {

    private String requestId;
    private String orderHash;

    public OrderRequest(String requestId, String orderHash) {
        this.requestId = requestId;
        this.orderHash = orderHash;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOrderHash() {
        return orderHash;
    }

    public void setOrderHash(String orderHash) {
        this.orderHash = orderHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(requestId, that.requestId) &&
                Objects.equals(orderHash, that.orderHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, orderHash);
    }

}

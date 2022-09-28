package net.brightlizard.shop.event.model.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class OrderEventModel implements Serializable {

    private String id;
    private String customer;
    private List<ShortItemEventModel> shortItems;
    private List<ItemEventModel> orderedItems = new ArrayList<>();
    private OrderStatusEventModel status = OrderStatusEventModel.INITIALIZED;
    private OrderCommStatusEventModel commStatus = OrderCommStatusEventModel.NO_COMMUNICATION_YET;
    private double totalPrice = 0;
    private String requestId;
    private LocalDateTime deliveryTime;

    public OrderEventModel(String id, String customer, OrderStatusEventModel status, OrderCommStatusEventModel commStatus, double totalPrice, String requestId, LocalDateTime deliveryTime) {
        this.id = id;
        this.customer = customer;
        this.status = status;
        this.commStatus = commStatus;
        this.totalPrice = totalPrice;
        this.requestId = requestId;
        this.deliveryTime = deliveryTime;
    }

    public OrderEventModel(String requestId, String customer, List<ShortItemEventModel> shortItems, LocalDateTime deliveryTime) {
        this.customer = customer;
        this.requestId = requestId;
        this.shortItems = shortItems;
        this.deliveryTime = deliveryTime;
    }

    public OrderEventModel(OrderEventModel order, List<ItemEventModel> items) {
        setId(order.getId());
        setCustomer(order.getCustomer());
        setOrderedItems(items);
        setStatus(order.getStatus());
        setCommStatus(order.getCommStatus());
        setTotalPrice(order.getTotalPrice());
        setRequestId(order.getRequestId());
        setDeliveryTime(order.getDeliveryTime());
    }

    public List<ItemEventModel> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<ItemEventModel> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public OrderStatusEventModel getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEventModel status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<ShortItemEventModel> getShortItems() {
        return shortItems;
    }

    public void setShortItems(List<ShortItemEventModel> shortItems) {
        this.shortItems = shortItems;
    }

    public OrderCommStatusEventModel getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(OrderCommStatusEventModel commStatus) {
        this.commStatus = commStatus;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEventModel)) return false;
        OrderEventModel order = (OrderEventModel) o;
        return Double.compare(order.totalPrice, totalPrice) == 0 && Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(shortItems, order.shortItems) && Objects.equals(orderedItems, order.orderedItems) && status == order.status && commStatus == order.commStatus && Objects.equals(requestId, order.requestId) && Objects.equals(deliveryTime, order.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, shortItems, orderedItems, status, commStatus, totalPrice, requestId, deliveryTime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", consumer='" + customer + '\'' +
                ", shortItems=" + shortItems +
                ", orderedItems=" + orderedItems +
                ", status=" + status +
                ", commStatus=" + commStatus +
                ", totalPrice=" + totalPrice +
                ", requestId='" + requestId + '\'' +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}

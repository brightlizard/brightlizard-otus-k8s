package net.brightlizard.shop.core.application.order.model;

import net.brightlizard.shop.core.application.storage.model.Item;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class Order implements Serializable {

    private String id;
    private String consumer;
    private List<ShortItem> shortItems;
    private List<Item> orderedItems = new ArrayList<>();
    private OrderStatus status = OrderStatus.INITIALIZED;
    private OrderCommStatus commStatus = OrderCommStatus.NO_COMMUNICATION_YET;
    private double totalPrice = 0;
    private String requestId;
    private LocalDateTime deliveryTime;

    public Order(String requestId, String consumer, List<ShortItem> shortItems, LocalDateTime deliveryTime) {
        this.consumer = consumer;
        this.requestId = requestId;
        this.shortItems = shortItems;
        this.deliveryTime = deliveryTime;
    }

    public Order(Order order, List<Item> items) {
        setId(order.getId());
        setConsumer(order.getConsumer());
        setOrderedItems(items);
        setStatus(order.getStatus());
        setCommStatus(order.getCommStatus());
        setTotalPrice(order.getTotalPrice());
        setRequestId(order.getRequestId());
        setDeliveryTime(order.getDeliveryTime());
    }

    public List<Item> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<Item> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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

    public List<ShortItem> getShortItems() {
        return shortItems;
    }

    public void setShortItems(List<ShortItem> shortItems) {
        this.shortItems = shortItems;
    }

    public OrderCommStatus getCommStatus() {
        return commStatus;
    }

    public void setCommStatus(OrderCommStatus commStatus) {
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
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Double.compare(order.totalPrice, totalPrice) == 0 && Objects.equals(id, order.id) && Objects.equals(consumer, order.consumer) && Objects.equals(shortItems, order.shortItems) && Objects.equals(orderedItems, order.orderedItems) && status == order.status && commStatus == order.commStatus && Objects.equals(requestId, order.requestId) && Objects.equals(deliveryTime, order.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consumer, shortItems, orderedItems, status, commStatus, totalPrice, requestId, deliveryTime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", consumer='" + consumer + '\'' +
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

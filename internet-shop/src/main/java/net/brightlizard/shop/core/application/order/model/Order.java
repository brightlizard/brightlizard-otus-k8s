package net.brightlizard.shop.core.application.order.model;

import net.brightlizard.shop.core.application.storage.model.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private OrderCommStatus commStatus = OrderCommStatus.NO_COMMUNICATION;
    private double totalPrice = 0;
    private String requestId;

    public Order(String requestId, String consumer, List<ShortItem> shortItems) {
        this.consumer = consumer;
        this.requestId = requestId;
        this.shortItems = shortItems;
    }

    public Order(Order order, List<Item> items) {
        setId(order.getId());
        setConsumer(order.getConsumer());
        setOrderedItems(items);
        setStatus(order.getStatus());
        setCommStatus(order.getCommStatus());
        setTotalPrice(order.getTotalPrice());
        setRequestId(order.getRequestId());
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
}

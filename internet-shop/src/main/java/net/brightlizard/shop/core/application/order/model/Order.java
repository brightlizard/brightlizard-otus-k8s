package net.brightlizard.shop.core.application.order.model;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class Order {

    private String id;
    private String consumer;
    private List<String> itemsIds;
    private OrderStatus status;
    private double totalPrice = 0;
    private String requestId;

    public Order(String requestId, String consumer, List<String> itemsIds) {
        this.consumer = consumer;
        this.itemsIds = itemsIds;
        this.requestId = requestId;
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

    public List<String> getItemsIds() {
        return itemsIds;
    }

    public void setItemsIds(List<String> itemsIds) {
        this.itemsIds = itemsIds;
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
}

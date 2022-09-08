package net.brightlizard.shop.infrastructure.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class Order {

    @NotNull
    @JsonProperty(value = "customer", required = true)
    private String consumer;

    @Size(min = 1)
    @JsonProperty(value = "items", required = true)
    private List<@Valid ShortItem> items;

    @NotNull
    @JsonProperty(value = "deliveryTime", required = true)
    private LocalDateTime deliveryTime;

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public List<ShortItem> getItems() {
        return items;
    }

    public void setItems(List<ShortItem> items) {
        this.items = items;
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
        return Objects.equals(consumer, order.consumer) && Objects.equals(items, order.items) && Objects.equals(deliveryTime, order.deliveryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consumer, items, deliveryTime);
    }

}

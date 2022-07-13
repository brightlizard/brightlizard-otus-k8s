package net.brightlizard.otus.k8s.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class Order {

    @Null
    @JsonProperty("id")
    String id;

    @Null
    @JsonProperty("totalSum")
    double totalSum;

    @NotNull
    @JsonProperty("items")
    List<Item> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.totalSum, totalSum) == 0 &&
                Objects.equals(id, order.id) &&
                Objects.equals(items, order.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalSum, items);
    }
}

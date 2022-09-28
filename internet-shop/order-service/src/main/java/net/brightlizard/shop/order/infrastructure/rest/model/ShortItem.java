package net.brightlizard.shop.order.infrastructure.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class ShortItem {

    @NotNull
    @JsonProperty(value = "id", required = true)
    private String id;

    @NotNull
    @JsonProperty(value = "quantity", required = true)
    private int quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortItem)) return false;
        ShortItem shortItem = (ShortItem) o;
        return quantity == shortItem.quantity && Objects.equals(id, shortItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantity);
    }
}

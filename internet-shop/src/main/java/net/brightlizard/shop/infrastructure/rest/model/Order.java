package net.brightlizard.shop.infrastructure.rest.model;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class Order {

    @NotNull
    private String consumer;

    @NotNull
    private List<String> itemsIds;


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
}

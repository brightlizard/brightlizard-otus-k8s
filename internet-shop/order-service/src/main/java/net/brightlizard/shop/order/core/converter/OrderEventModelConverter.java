package net.brightlizard.shop.order.core.converter;

import net.brightlizard.shop.event.model.order.*;
import net.brightlizard.shop.order.core.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
@Service
public class OrderEventModelConverter {


    public OrderEventModel convert(Order order) {
        OrderEventModel orderEventModel = new OrderEventModel(
            order.getId(),
            order.getCustomer(),
            OrderStatusEventModel.valueOf(order.getStatus().name()),
            OrderCommStatusEventModel.valueOf(order.getCommStatus().name()),
            order.getTotalPrice(),
            order.getRequestId(),
            order.getDeliveryTime()
        );

        ArrayList<ShortItemEventModel> newShortItems = new ArrayList<>();
        order.getShortItems().forEach(shortItem -> {
            newShortItems.add(new ShortItemEventModel(shortItem.getId(), shortItem.getQuantity()));
        });
        orderEventModel.setShortItems(newShortItems);

        ArrayList<ItemEventModel> newOrderedItems = new ArrayList<>();
        order.getOrderedItems().forEach(orderedItem -> {
            newOrderedItems.add(new ItemEventModel(orderedItem.getId(), orderedItem.getName(), orderedItem.getPrice(), orderedItem.getQuantity()));
        });
        orderEventModel.setOrderedItems(newOrderedItems);
        return orderEventModel;
    }

    public Order convert(OrderEventModel orderEventModel){
        Order order = new Order(
            orderEventModel.getId(),
            orderEventModel.getCustomer(),
            OrderStatus.valueOf(orderEventModel.getStatus().name()),
            OrderCommStatus.valueOf(orderEventModel.getCommStatus().name()),
            orderEventModel.getTotalPrice(),
            orderEventModel.getRequestId(),
            orderEventModel.getDeliveryTime()
        );

        ArrayList<ShortItem> newShortItems = new ArrayList<>();
        orderEventModel.getShortItems().forEach(item -> {
            newShortItems.add(new ShortItem(item.getId(), item.getQuantity()));
        });
        order.setShortItems(newShortItems);

        ArrayList<Item> newOrderedItems = new ArrayList<>();
        orderEventModel.getOrderedItems().forEach(item -> {
            newOrderedItems.add(new Item(item.getId(), item.getName(), item.getPrice(), item.getQuantity()));
        });
        order.setOrderedItems(newOrderedItems);

        return order;
    }
}

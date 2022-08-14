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

}

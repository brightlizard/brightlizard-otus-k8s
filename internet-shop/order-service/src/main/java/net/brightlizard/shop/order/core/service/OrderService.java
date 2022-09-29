package net.brightlizard.shop.order.core.service;

import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.OrderStatus;
import net.brightlizard.shop.order.core.model.RequestStatus;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface OrderService {

    RequestStatus createRequest(Order order);
    OrderStatus createOrder(Order order);
    List<Order> getResults();
    List<String> getRequests();
    Order calculateTotalSum(Order order);

}

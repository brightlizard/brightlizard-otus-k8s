package net.brightlizard.shop.core.application.order;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.model.RequestStatus;
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

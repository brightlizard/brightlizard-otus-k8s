package net.brightlizard.shop.order.core.facade;

import net.brightlizard.shop.order.core.model.Order;
import net.brightlizard.shop.order.core.model.RequestStatus;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface OrderFacade {

    RequestStatus handleRequest(Order order);
    List<Order> getResults();
    List<String> getRequests();

}

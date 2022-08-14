package net.brightlizard.shop.core.application;

import net.brightlizard.shop.core.application.order.model.RequestStatus;
import net.brightlizard.shop.infrastructure.rest.model.Order;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface OrderFacade {

    RequestStatus handleRequest(Order order);
    List<Order> getResults();

}

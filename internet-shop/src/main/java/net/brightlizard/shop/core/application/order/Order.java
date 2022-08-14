package net.brightlizard.shop.core.application.order;

import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.model.RequestStatus;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface Order {

    RequestStatus createRequest();
    OrderStatus createOrder();
    Object getResults();

}

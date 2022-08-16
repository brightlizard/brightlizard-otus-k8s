package net.brightlizard.shop.core.application.delivery;

import net.brightlizard.shop.core.application.order.model.Order;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface DeliveryService {

    Order schedule(Order order);

}

package net.brightlizard.shop.core.application.delivery;

import net.brightlizard.shop.event.model.order.OrderEventModel;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface DeliveryService {

    OrderEventModel schedule(OrderEventModel order);

}

package net.brightlizard.shop.core.application.storage;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.storage.model.ReservationResult;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface StorageService {
    Order reserve(Order order);

}

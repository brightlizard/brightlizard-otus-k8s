package net.brightlizard.shop.storage.core.service;

import net.brightlizard.shop.event.model.order.OrderEventModel;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface StorageService {
    OrderEventModel reserve(OrderEventModel order);
    OrderEventModel rollback(OrderEventModel order);
}

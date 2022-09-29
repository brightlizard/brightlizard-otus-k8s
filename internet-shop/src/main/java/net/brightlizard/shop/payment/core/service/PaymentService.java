package net.brightlizard.shop.payment.core.service;

import net.brightlizard.shop.event.model.order.OrderEventModel;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface PaymentService {
    void process(OrderEventModel order);
    void rollback(OrderEventModel order);
}
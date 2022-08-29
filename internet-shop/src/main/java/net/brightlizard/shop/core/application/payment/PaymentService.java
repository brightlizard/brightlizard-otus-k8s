package net.brightlizard.shop.core.application.payment;

import net.brightlizard.shop.core.application.order.model.Order;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface PaymentService {
    void process(Order order);
    void rollback(Order order);
}

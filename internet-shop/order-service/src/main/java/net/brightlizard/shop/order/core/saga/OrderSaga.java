package net.brightlizard.shop.order.core.saga;

import net.brightlizard.shop.order.core.model.Order;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public interface OrderSaga {

    Order startFor(Order order);
    void runStorageReserve(Order order);
    void handleStorageReserveReply(Order order);
    void runPaymentDo(Order order);
    void handlePaymentDoReply(Order order);
    void handleScheduleDeliveryReply(Order order);
    void handlePaymentRollbackReply(Order order);
    void handleReservationRollbackReply(Order order);

}

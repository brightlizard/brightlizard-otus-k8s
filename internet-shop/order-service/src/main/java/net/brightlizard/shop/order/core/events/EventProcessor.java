package net.brightlizard.shop.order.core.events;

import net.brightlizard.shop.event.model.notification.NotificationStatusEventModel;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.order.core.model.Order;
import java.util.function.Consumer;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public interface EventProcessor {

    void runStorageReserve(Order order, Consumer<Order> replyHandler);
    void handleStorageReserveReply(OrderEventModel orderEventModel);
    void runPaymentDo(Order order, Consumer<Order> replyHandler);
    void handlePaymentDoReply(OrderEventModel orderEventModel);
    void runScheduleDelivery(Order order, Consumer<Order> replyHandler);
    void handleScheduleDeliveryReply(OrderEventModel orderEventModel);
    void runSendNotification(Order order, NotificationStatusEventModel everythingGood);
    void runRollbackReservation(Order order, Consumer<Order> replyHandler);
    void runRollbackPayment(Order order, Consumer<Order> replyHandler);
    void handlePaymentRollbackReply(OrderEventModel orderEventModel);
    void handleReservationRollbackReply(OrderEventModel orderEventModel);

}

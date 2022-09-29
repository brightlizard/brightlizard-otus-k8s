package net.brightlizard.shop.order.infrastructure.events;

import net.brightlizard.shop.event.model.notification.NotificationStatusEventModel;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.order.core.events.EventProcessor;
import net.brightlizard.shop.order.core.model.Order;
import java.util.function.Consumer;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class KafkaEventProcessor implements EventProcessor {

    @Override
    public void runStorageReserve(Order order, Consumer<Order> replyHandler) {

    }

    @Override
    public void handleStorageReserveReply(OrderEventModel orderEventModel) {

    }

    @Override
    public void runPaymentDo(Order order, Consumer<Order> replyHandler) {

    }

    @Override
    public void handlePaymentDoReply(OrderEventModel orderEventModel) {

    }

    @Override
    public void runScheduleDelivery(Order order, Consumer<Order> replyHandler) {

    }

    @Override
    public void handleScheduleDeliveryReply(OrderEventModel orderEventModel) {

    }

    @Override
    public void runSendNotification(Order order, NotificationStatusEventModel everythingGood) {

    }

    @Override
    public void runRollbackReservation(Order order, Consumer<Order> replyHandler) {

    }

    @Override
    public void runRollbackPayment(Order order, Consumer<Order> replyHandler) {

    }

    @Override
    public void handlePaymentRollbackReply(OrderEventModel orderEventModel) {

    }

    @Override
    public void handleReservationRollbackReply(OrderEventModel orderEventModel) {

    }
}

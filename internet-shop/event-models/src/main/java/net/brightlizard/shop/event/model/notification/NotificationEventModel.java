package net.brightlizard.shop.event.model.notification;

import java.io.Serializable;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class NotificationEventModel implements Serializable {

    private String orderId;
    private NotificationStatusEventModel notificationStatus;

    public NotificationEventModel(String orderId, NotificationStatusEventModel notificationStatus) {
        this.orderId = orderId;
        this.notificationStatus = notificationStatus;
    }

    public NotificationStatusEventModel getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatusEventModel notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}


package net.brightlizard.shop.notification.core.model;

import java.io.Serializable;

/**
 * @author Ovcharov Ilya (ovcharov.ilya@gmail.com)
 * net.brightlizard (c)
 */
public class Notification implements Serializable {

    private String orderId;
    private NotificationStatus notificationStatus;

    public Notification(String orderId, NotificationStatus notificationStatus) {
        this.orderId = orderId;
        this.notificationStatus = notificationStatus;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}


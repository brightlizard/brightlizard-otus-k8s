package net.brightlizard.shop.notification.core.repository;

import net.brightlizard.shop.notification.core.model.Notification;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface NotificationRepository /*extends Repository<Order, String>*/ {

    Notification save(Notification notification);
    List<Notification> findAll();
}

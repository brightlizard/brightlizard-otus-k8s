package net.brightlizard.shop.core.application.notification;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.core.application.notification.model.Notification;
import net.brightlizard.shop.core.application.notification.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 *
 * Payment Actor
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NotificationReactor extends AbstractVerticle {

    private Logger LOGGER = LoggerFactory.getLogger(NotificationReactor.class);

    private Vertx vertx;
    private EventBus eventBus;
    private NotificationRepository notificationRepository;

    public NotificationReactor(Vertx vertx, NotificationRepository notificationRepository) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        eventBus.consumer("notification.billing", notificationsFromBilling());
        startPromise.complete();
    }

    private Handler<Message<Object>> notificationsFromBilling() {
        return message -> {
            LOGGER.info("NOTIFICATION FROM BILLING");
            try {
                Notification notification = (Notification) SerializationUtils.deserialize((byte[]) message.body());
                notificationRepository.save(notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

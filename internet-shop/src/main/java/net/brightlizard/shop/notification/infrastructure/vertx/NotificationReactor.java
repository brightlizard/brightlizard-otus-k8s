package net.brightlizard.shop.notification.infrastructure.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import net.brightlizard.shop.notification.core.model.Notification;
import net.brightlizard.shop.notification.core.model.NotificationStatus;
import net.brightlizard.shop.notification.core.repository.NotificationRepository;
import net.brightlizard.shop.event.model.notification.NotificationEventModel;
import net.brightlizard.shop.configuration.VertxContainer;
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

    public NotificationReactor(VertxContainer vertxContainer, NotificationRepository notificationRepository) {
        this.vertx = vertxContainer.getVertx();
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
                NotificationEventModel notification = (NotificationEventModel) SerializationUtils.deserialize((byte[]) message.body());
                Notification notification1 = new Notification(notification.getOrderId(), NotificationStatus.valueOf(notification.getNotificationStatus().name()));
                notificationRepository.save(notification1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

package net.brightlizard.shop.notification.core.repository;

import net.brightlizard.shop.notification.core.model.Notification;
import net.brightlizard.shop.notification.core.model.NotificationStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private JdbcTemplate jdbcTemplate;

    public NotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Notification save(Notification notification) {
        String SQL = "INSERT INTO internet_shop.public.notification(order_id, status) VALUES (?,?)";
        jdbcTemplate.update(
            SQL,
            notification.getOrderId(),
            notification.getNotificationStatus().toString()
        );

        return notification;
    }

    @Override
    public List<Notification> findAll() {
        String SQL = "SELECT * FROM internet_shop.public.notification";
        List<Notification> notifications = jdbcTemplate.query(
            SQL, (rs, rowNum) -> new Notification(
                    rs.getString("order_id"),
                    NotificationStatus.valueOf(rs.getString("status"))
                )
        );

        return notifications;
    }

}

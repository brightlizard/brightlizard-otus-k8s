package net.brightlizard.shop.infrastructure.rest;

import net.brightlizard.shop.core.application.notification.model.Notification;
import net.brightlizard.shop.core.application.notification.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Notification>> getAccounts(){
        List<Notification> notifications = notificationRepository.findAll();
        return new ResponseEntity(notifications, HttpStatus.OK);
    }

}

package net.brightlizard.shop.core.application.delivery;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private Logger LOGGER = LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private HashMap<LocalDateTime, LocalDateTime> scheduledTime = new HashMap<>(){{
        put(LocalDateTime.parse("2022-08-17T12:00:00"), LocalDateTime.parse("2022-08-17T13:00:00"));
        put(LocalDateTime.parse("2022-08-17T18:00:00"), LocalDateTime.parse("2022-08-17T19:00:00"));
    }};

    @Override
    public Order schedule(Order order) {

        LocalDateTime deliveryTime = order.getDeliveryTime();

        boolean intersection = false;

        for (Map.Entry<LocalDateTime, LocalDateTime> entry : scheduledTime.entrySet()) {
            LocalDateTime p1 = entry.getKey();
            LocalDateTime p2 = entry.getValue();
            if (
                (deliveryTime.equals(p1) || deliveryTime.isAfter(p1)) &&
                deliveryTime.isBefore(p2)
            ) {
                intersection = true;
            }
        }

        if(intersection == true){
            order.setStatus(OrderStatus.DELIVERY_ERROR);
            return order;
        }

        scheduledTime.put(deliveryTime, deliveryTime.plusHours(1));

        LOGGER.info("SCHEDULED TIME: {}", scheduledTime);
        order.setStatus(OrderStatus.DELIVERY_SUCCESS);
        return order;
    }

}

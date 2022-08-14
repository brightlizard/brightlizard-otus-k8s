package net.brightlizard.shop.core.application.order;

import io.vertx.core.AbstractVerticle;
import net.brightlizard.shop.core.application.order.model.OrderCreatedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class OrderReactor extends AbstractVerticle implements ApplicationListener<OrderCreatedEvent> {



    @Override
    public void onApplicationEvent(OrderCreatedEvent event) {

    }

}

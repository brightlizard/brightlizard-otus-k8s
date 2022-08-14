package net.brightlizard.shop.core.application.order.model;

import org.springframework.context.ApplicationEvent;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public class OrderCreatedEvent extends ApplicationEvent {

    private Order source;

    public OrderCreatedEvent(Order source) {
        super(source);
        this.source = source;
    }

    @Override
    public Order getSource() {
        return source;
    }

}

package net.brightlizard.shop.core.application;

import net.brightlizard.shop.core.application.order.model.RequestStatus;
import net.brightlizard.shop.infrastructure.rest.model.Order;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class OrderFacadeImpl implements OrderFacade {

    @Override
    public RequestStatus handleRequest(Order order) {

        return null;
    }

    @Override
    public List<Order> getResults() {

        return null;
    }

}

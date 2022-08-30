package net.brightlizard.shop.core.application.order.repository;

import net.brightlizard.shop.core.application.order.model.Order;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface OrderRepository /*extends Repository<Order, String>*/ {

    Optional<Order> findById(String id);
    Order save(Order order);
    Order update(Order order);
    Order updateStatusAndItems(Order order);
    Order updateStatus(Order order);
    List<Order> findAll();
}

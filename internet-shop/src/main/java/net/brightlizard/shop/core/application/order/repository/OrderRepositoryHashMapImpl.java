package net.brightlizard.shop.core.application.order.repository;

import net.brightlizard.shop.core.application.order.model.Order;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class OrderRepositoryHashMapImpl implements OrderRepository {

    private HashMap<String, Order> ordersById = new HashMap();

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(ordersById.get(id));
    }

    @Override
    public Order save(Order order) {
        order.setId(UUID.randomUUID().toString());
        ordersById.put(order.getId(), order);
        return order;
    }

    @Override
    public Order update(Order order) {
        ordersById.put(order.getId(), order);
        return null;
    }

    @Override
    public List<Order> findAll() {
        return ordersById.values().stream().collect(Collectors.toList());
    }

}

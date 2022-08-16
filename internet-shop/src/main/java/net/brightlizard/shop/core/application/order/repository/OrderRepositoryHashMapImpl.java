package net.brightlizard.shop.core.application.order.repository;

import net.brightlizard.shop.core.application.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
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
        return ordersById.put(order.getId(), order);
    }

    @Override
    public Order updateStatusAndItems(Order order) {
        Order order1 = ordersById.get(order.getId());
        order1.setStatus(order.getStatus());
        order1.setOrderedItems(order.getOrderedItems());
        return ordersById.put(order.getId(), order1);
    }

    @Override
    public Order updateStatus(Order order) {
        Order order1 = ordersById.get(order.getId());
        order1.setStatus(order.getStatus());
        return ordersById.put(order.getId(), order1);
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(ordersById.values());
    }

}

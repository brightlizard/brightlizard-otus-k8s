package net.brightlizard.shop.core.application.order.repository;

import net.brightlizard.shop.core.application.order.model.*;
import net.brightlizard.shop.core.application.storage.model.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Order> findById(String id) {
        Order order = jdbcTemplate.queryForObject(
                "SELECT * FROM internet_shop.public.order WHERE id = ?",
                Order.class,
                id
        );

        return Optional.ofNullable(order);
    }

    @Override
    public Order save(Order order) {
        order.setId(UUID.randomUUID().toString());
        String SQL = "INSERT INTO internet_shop.public.order(id, customer, itemids, status, commstatus, totalprice, requestid, deliverytime) VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
            SQL,
            order.getId(),
            order.getCustomer(),
            String.join(",", order.getShortItems().stream().map(ShortItem::getId).collect(Collectors.toList())),
            order.getStatus().toString(),
            order.getCommStatus().toString(),
            order.getTotalPrice(),
            order.getRequestId(),
            order.getDeliveryTime().toString()
        );

        order.getShortItems().forEach(shortItem -> {
            String SQL2 = "INSERT INTO internet_shop.public.short_item(id, quantity, orderid) VALUES (?,?,?)";
            jdbcTemplate.update(
                SQL2,
                shortItem.getId(),
                shortItem.getQuantity(),
                order.getId()
            );
        });

        return order;
    }

    @Override
    public Order update(Order order) {
        String SQL = "UPDATE internet_shop.public.order " +
                     "SET customer = ?, " +
                     "itemids = ?, " +
                     "status = ?, " +
                     "commstatus = ?, " +
                     "totalprice = ?, " +
                     "requestid = ?, " +
                     "deliverytime = ? " +
                     "WHERE id = ?";
        jdbcTemplate.update(
            SQL,
            order.getCustomer(),
            String.join(",", order.getShortItems().stream().map(ShortItem::getId).collect(Collectors.toList())),
            order.getStatus().toString(),
            order.getCommStatus().toString(),
            order.getTotalPrice(),
            order.getRequestId(),
            order.getDeliveryTime(),
            order.getId()
        );

        return order;
    }

    @Override
    public Order updateStatusAndItems(Order order) {
        String SQL = "UPDATE internet_shop.public.order " +
                "SET status = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
            SQL,
            order.getStatus().toString(),
            order.getId()
        );

        order.getOrderedItems().forEach(orderedItem -> {
            String SQL2 = "INSERT INTO internet_shop.public.ordered_item(id, name, price, quantity, orderid) VALUES (?,?,?,?,?)";
            jdbcTemplate.update(
                SQL2,
                orderedItem.getId(),
                orderedItem.getName(),
                orderedItem.getPrice(),
                orderedItem.getQuantity(),
                order.getId()
            );
        });

        return order;
    }

    @Override
    public Order updateStatus(Order order) {
        String SQL = "UPDATE internet_shop.public.order " +
                "SET status = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(
            SQL,
            order.getStatus().toString(),
            order.getId()
        );

        return order;
    }

    @Override
    public List<Order> findAll() {
        String SQL = "SELECT * FROM internet_shop.public.order";
        List<Order> orders = jdbcTemplate.query(
            SQL, (rs, rowNum) -> new Order(
                    rs.getString("id"),
                        rs.getString("customer"),
                        OrderStatus.valueOf(rs.getString("status")),
                        OrderCommStatus.valueOf(rs.getString("commstatus")),
                        rs.getDouble("totalprice"),
                        rs.getString("requestid"),
                        LocalDateTime.parse(rs.getString("deliverytime").replace(" ", "T"))
                )
        );

        orders.forEach(order -> {
            String SQL2 = "SELECT * FROM internet_shop.public.ordered_item WHERE orderid = ?";
            List<Item> items = jdbcTemplate.query(
                    SQL2, (rs, rowNum) -> new Item(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ), order.getId()
            );
            order.setOrderedItems(items);
        });

        return orders;
    }

}

package net.brightlizard.shop.storage.core.repolsitory;

import net.brightlizard.shop.storage.core.model.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private JdbcTemplate jdbcTemplate;

    public ItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean containsAllItems(List<String> itemIds) {
        String inSql = String.join(",", Collections.nCopies(itemIds.size(), "?"));

        Integer foundedCount = jdbcTemplate.queryForObject(
            String.format("SELECT count(*) FROM internet_shop.public.item WHERE id IN (%s)", inSql),
            Integer.class,
            itemIds.toArray()
        );

        return itemIds.size() == foundedCount;
    }

    @Override
    public List<Item> findAllItems(List<String> itemIds) {
        String inSql = String.join(",", Collections.nCopies(itemIds.size(), "?"));

        String SQL = String.format("SELECT * FROM internet_shop.public.item WHERE id IN (%s)", inSql);
        List<Item> items = jdbcTemplate.query(
                SQL, (rs, rowNum) -> new Item(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ),
                itemIds.toArray()
        );

        return items;
    }

    @Override
    public List<Item> findAll() {
        String SQL = "SELECT * FROM internet_shop.public.item";
        List<Item> items = jdbcTemplate.query(
                SQL, (rs, rowNum) -> new Item(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                )
        );

        return items;
    }

    @Override
    public List<Item> updateQuantity(List<Item> items) {
        items.forEach(item -> {
            String SQL = "UPDATE internet_shop.public.item " +
                    "SET quantity = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(
                SQL,
                item.getQuantity(),
                item.getId()
            );
        });
        return items;
    }

}

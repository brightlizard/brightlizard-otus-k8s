package net.brightlizard.shop.core.application.storage.repolsitory;

import net.brightlizard.shop.core.application.storage.model.Item;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private HashMap<String, Item> itemsById = new HashMap<>();

    @Override
    public boolean containsAllItems(List<String> itemIds) {
        return itemsById.entrySet().containsAll(itemIds);
    }

    @Override
    public List<Item> findAllItems(List<String> itemIds) {
        HashMap<String, Item> tmpItems = new HashMap<>();
        itemsById.forEach((k, v) -> {
            if(itemIds.contains(k))  tmpItems.put(k, v);
        });
        return new ArrayList<>(tmpItems.values());
    }

    @Override
    public List<Item> save(List<Item> items) {
        items.forEach(item -> {
            itemsById.put(item.getId(), item);
        });
        return new ArrayList<>(itemsById.values());
    }

    @Override
    public List<Item> update(List<Item> items) {
        items.forEach(item -> {
            itemsById.get(item.getId()).setQuantity(item.getQuantity());
        });
        return new ArrayList<>(itemsById.values());
    }

}

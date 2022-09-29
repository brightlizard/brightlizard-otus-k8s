package net.brightlizard.shop.storage.core.repolsitory;

import net.brightlizard.shop.storage.core.model.Item;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Repository
public class ReservedItemRepositoryImpl implements ReservedItemRepository {

    private HashMap<String, Item> itemsById = new HashMap<>();

    @Override
    public boolean containsAllItems(List<String> itemIds) {
        return itemsById.keySet().containsAll(itemIds);
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
    public List<Item> findAll() {
        return new ArrayList<>(itemsById.values());
    }

    @Override
    public List<Item> update(List<Item> items) {
        items.forEach(item -> {
            itemsById.put(item.getId(), item);
        });
        return new ArrayList<>(itemsById.values());
    }

}

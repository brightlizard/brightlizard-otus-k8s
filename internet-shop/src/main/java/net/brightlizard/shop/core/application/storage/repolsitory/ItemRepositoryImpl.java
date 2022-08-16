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

    private HashMap<String, Item> itemsById = new HashMap<>(){{
        put("cabc1e54-b694-4117-8782-cacae7a60fcc", new Item("cabc1e54-b694-4117-8782-cacae7a60fcc", "Majesty Skis Warewolf 182", 43000.0, 5));
        put("3b2d021f-4c59-4988-bfb8-29e348ce8a84", new Item("3b2d021f-4c59-4988-bfb8-29e348ce8a84", "Fox Youth Comp Boots", 16000.0, 7));
        put("f4f5b142-3d73-4b32-b876-4e9baace0dd2", new Item("f4f5b142-3d73-4b32-b876-4e9baace0dd2", "Full Tilt 120 Alpine Boots", 27500.0, 2));
        put("b8e4fd90-abb4-4cb1-92bf-452c91a757a0", new Item("b8e4fd90-abb4-4cb1-92bf-452c91a757a0", "Seba Skates FRX", 14600.0, 1));
        put("07b676b3-5953-471d-9f20-26c48fe0bab1", new Item("07b676b3-5953-471d-9f20-26c48fe0bab1", "Something Awesome", 500.0, 143));
        put("92e59fbf-d6e1-4636-bc3b-825cc7390be8", new Item("92e59fbf-d6e1-4636-bc3b-825cc7390be8", "Cheapest Thing", 15.0, 1856));
    }};

    @Override
    public boolean containsAllItems(List<String> itemIds) {
        return itemsById.keySet().containsAll(itemIds);
    }

    @Override
    public List<Item> findAllItems(List<String> itemIds) {
        ArrayList<Item> items = new ArrayList<>();
        itemsById.forEach((k, v) -> {
            if(itemIds.contains(k))  items.add(new Item(v));
        });
        return items;
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(itemsById.values());
    }

    @Override
    public List<Item> updateQuantity(List<Item> items) {
        items.forEach(item -> {
            itemsById.get(item.getId()).setQuantity(item.getQuantity());
        });
        return new ArrayList<>(itemsById.values());
    }

}

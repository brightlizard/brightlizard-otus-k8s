package net.brightlizard.shop.storage.core.repolsitory;

import net.brightlizard.shop.storage.core.model.Item;

import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface ItemRepository /*extends Repository<Item, String>*/ {

    boolean containsAllItems(List<String> itemIds);
    List<Item> findAllItems(List<String> itemIds);
    List<Item> findAll();
    List<Item> updateQuantity(List<Item> items);

}

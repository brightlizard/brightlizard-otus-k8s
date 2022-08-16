package net.brightlizard.shop.core.application.storage.repolsitory;

import net.brightlizard.shop.core.application.storage.model.Item;
import org.springframework.data.repository.Repository;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface ReservedItemRepository extends Repository<Item, String> {

    boolean containsAllItems(List<String> itemIds);
    List<Item> findAllItems(List<String> itemIds);
    List<Item> findAll();
    List<Item> update(List<Item> items);

}

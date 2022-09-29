package net.brightlizard.shop.storage.core.facade;

import net.brightlizard.shop.storage.core.model.Item;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface StorageFacade {
    List<Item> getItems();
}

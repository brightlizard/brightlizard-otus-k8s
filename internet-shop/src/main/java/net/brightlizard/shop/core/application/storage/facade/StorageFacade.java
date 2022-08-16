package net.brightlizard.shop.core.application.storage.facade;

import net.brightlizard.shop.core.application.storage.model.Item;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
public interface StorageFacade {
    List<Item> getItems();
}

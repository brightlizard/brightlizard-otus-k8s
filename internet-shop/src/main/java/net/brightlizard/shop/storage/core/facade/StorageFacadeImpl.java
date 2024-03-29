package net.brightlizard.shop.storage.core.facade;

import net.brightlizard.shop.storage.core.model.Item;
import net.brightlizard.shop.storage.core.repolsitory.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class StorageFacadeImpl implements StorageFacade {

    private ItemRepository itemRepository;

    public StorageFacadeImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAll();
    }
}

package net.brightlizard.shop.core.application.storage;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.ShortItem;
import net.brightlizard.shop.core.application.storage.model.Item;
import net.brightlizard.shop.core.application.storage.model.ReservationResult;
import net.brightlizard.shop.core.application.storage.repolsitory.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ovcharov Ilya (IAOvcharov@sberbank.ru; ovcharov.ilya@gmail.com)
 * @author SberAPI Team
 */
@Service
public class StorageServiceImpl implements StorageService {

    private ItemRepository itemRepository;

    public StorageServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ReservationResult reserve(Order order) {
        try {
            List<ShortItem> shortItems = order.getShortItems();
            List<String> shortItemIds = shortItems.stream().map(ShortItem::getId).collect(Collectors.toList());
            Map<String, Integer> quantityById = shortItems.stream().collect(Collectors.toMap(ShortItem::getId, ShortItem::getQuantity));

            if(!itemRepository.containsAllItems(shortItemIds)){
                return ReservationResult.ERROR;
            }

            List<Item> allItems = itemRepository.findAllItems(shortItemIds);

            allItems.forEach(item -> {
                int reserverdQuantity = quantityById.get(item.getId());
                int result = item.getQuantity() - reserverdQuantity;
                if(result < 0) throw new RuntimeException("Trying to reserve more items than available.");
                item.setQuantity(result);
            });

            itemRepository.update(allItems);
            return ReservationResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ReservationResult.ERROR;
        }
    }

}

package net.brightlizard.shop.core.application.storage;

import net.brightlizard.shop.core.application.order.model.Order;
import net.brightlizard.shop.core.application.order.model.OrderStatus;
import net.brightlizard.shop.core.application.order.model.ShortItem;
import net.brightlizard.shop.core.application.storage.model.Item;
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
    public Order reserve(Order order) {
        try {
            List<ShortItem> shortItems = order.getShortItems();
            List<String> shortItemIds = shortItems.stream().map(ShortItem::getId).collect(Collectors.toList());
            Map<String, Integer> quantityById = shortItems.stream().collect(Collectors.toMap(ShortItem::getId, ShortItem::getQuantity));

            if(!itemRepository.containsAllItems(shortItemIds)){
                order.setStatus(OrderStatus.STORAGE_RESERVE_ERROR);
                return order;
            }

            List<Item> allItems = itemRepository.findAllItems(shortItemIds);

            allItems.forEach(item -> {
                int reservedQuantity = quantityById.get(item.getId());
                int result = item.getQuantity() - reservedQuantity;
                if(result < 0) throw new RuntimeException("Trying to reserve more items than available.");
                item.setQuantity(result);
            });
            itemRepository.updateQuantity(allItems);

            List<Item> updatedItems = itemRepository.findAllItems(shortItemIds);
            updatedItems.forEach(item -> {
                item.setQuantity(quantityById.get(item.getId()));
            });
            // TODO: reserved
            order.setOrderedItems(updatedItems);
            order.setStatus(OrderStatus.STORAGE_RESERVE_SUCCESS);
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            order.setStatus(OrderStatus.STORAGE_RESERVE_ERROR);
            return order;
        }
    }

    @Override
    public Order rollback(Order order) {
        List<ShortItem> shortItems = order.getShortItems();
        List<String> shortItemIds = shortItems.stream().map(ShortItem::getId).collect(Collectors.toList());
        Map<String, Integer> quantityById = shortItems.stream().collect(Collectors.toMap(ShortItem::getId, ShortItem::getQuantity));

        List<Item> allItems = itemRepository.findAllItems(shortItemIds);
        allItems.forEach(item -> {
            int reservedQuantity = quantityById.get(item.getId());
            int result = item.getQuantity() + reservedQuantity;
            item.setQuantity(result);
        });
        itemRepository.updateQuantity(allItems);
        order.setStatus(OrderStatus.STORAGE_RESERVE_ROLLBACK);
        return order;
    }

}

package net.brightlizard.shop.core.application.storage;

import net.brightlizard.shop.event.model.order.ItemEventModel;
import net.brightlizard.shop.event.model.order.OrderEventModel;
import net.brightlizard.shop.event.model.order.OrderStatusEventModel;
import net.brightlizard.shop.event.model.order.ShortItemEventModel;
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
    public OrderEventModel reserve(OrderEventModel order) {
        try {
            List<ShortItemEventModel> shortItems = order.getShortItems();
            List<String> shortItemIds = shortItems.stream().map(ShortItemEventModel::getId).collect(Collectors.toList());
            Map<String, Integer> quantityById = shortItems.stream().collect(Collectors.toMap(ShortItemEventModel::getId, ShortItemEventModel::getQuantity));

            if(!itemRepository.containsAllItems(shortItemIds)){
                order.setStatus(OrderStatusEventModel.STORAGE_RESERVE_ERROR);
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
            List<ItemEventModel> itemEventModelList = updatedItems.stream()
                    .map(item -> new ItemEventModel(item.getId(), item.getName(), item.getPrice(), item.getQuantity()))
                    .collect(Collectors.toList());
            // TODO: reserved
            order.setOrderedItems(itemEventModelList);
            order.setStatus(OrderStatusEventModel.STORAGE_RESERVE_SUCCESS);
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            order.setStatus(OrderStatusEventModel.STORAGE_RESERVE_ERROR);
            return order;
        }
    }

    @Override
    public OrderEventModel rollback(OrderEventModel order) {
        List<ShortItemEventModel> shortItems = order.getShortItems();
        List<String> shortItemIds = shortItems.stream().map(ShortItemEventModel::getId).collect(Collectors.toList());
        Map<String, Integer> quantityById = shortItems.stream().collect(Collectors.toMap(ShortItemEventModel::getId, ShortItemEventModel::getQuantity));

        List<Item> allItems = itemRepository.findAllItems(shortItemIds);
        allItems.forEach(item -> {
            int reservedQuantity = quantityById.get(item.getId());
            int result = item.getQuantity() + reservedQuantity;
            item.setQuantity(result);
        });
        itemRepository.updateQuantity(allItems);
        order.setStatus(OrderStatusEventModel.STORAGE_RESERVE_ROLLBACK);
        return order;
    }

}

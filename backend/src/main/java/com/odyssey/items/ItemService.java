package com.odyssey.items;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemDao itemDao;

    public ItemService(@Qualifier("itemJPAService") ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public List<Item> getAllItems() {
        return itemDao.selectAllItems();
    }

    public Item getItem(Integer id) {
        return itemDao.selectItemById(id)
                .orElseThrow(() -> new ResourceNotFoundException("item with id [%s] not found".formatted(id)));
    }

    public Item getItemByName(String name) {
        return itemDao.selectItemByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("item with name [%s] not found".formatted(name)));
    }

    public void addItem(ItemRegistrationRequest request) {
        if (itemDao.existsItemByName(request.name())) {
            throw new DuplicateResourceException("item already exists");
        }

        Item item = new Item(request.name());

        itemDao.insertItem(item);
    }

    public void deleteItem(Integer id) {
        if (itemDao.existsItemById(id)) {
            itemDao.deleteItemById(id);
        }
        else {
            throw new ResourceNotFoundException("item with id [%s] not found".formatted(id));
        }
    }

    public void deleteItemByName(String name) {
        if (itemDao.existsItemByName(name)) {
            itemDao.deleteItemByName(name);
        }
        else {
            throw new ResourceNotFoundException("item with name [%s] not found".formatted(name));
        }
    }

    public void updateItem(Integer id, ItemUpdateRequest request) {
        Item existingItem = getItem(id);

        if (itemDao.existsItemByName(request.name())) {
            throw new DuplicateResourceException("item already exists");
        }

        boolean changes = false;

        if (request.name() != null && !request.name().equals(existingItem.getName())){
            existingItem.setName(request.name());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        itemDao.updateItem(existingItem);
    }

}

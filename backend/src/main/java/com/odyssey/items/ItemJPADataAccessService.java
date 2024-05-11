package com.odyssey.items;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("itemJPAService")
public class ItemJPADataAccessService implements ItemDao {

    private final ItemRepository itemRepository;

    public ItemJPADataAccessService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> selectAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> selectItemById(Integer id) {
        return itemRepository.findById(id);
    }

    @Override
    public Optional<Item> selectItemByName(String name) {
        return itemRepository.findItemByName(name);
    }

    @Override
    public void insertItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    public void updateItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    public boolean existsItemById(Integer id) {
        return itemRepository.existsItemById(id);
    }

    @Override
    public boolean existsItemByName(String name) {
        return itemRepository.existsItemByName(name);
    }

    @Override
    public void deleteItemById(Integer id) {
        itemRepository.deleteById(id);
    }

    @Override
    public void deleteItemByName(String name) {
        itemRepository.deleteItemByName(name);
    }
}

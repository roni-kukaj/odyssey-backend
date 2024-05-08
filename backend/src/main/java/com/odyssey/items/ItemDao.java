package com.odyssey.items;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    List<Item> selectAllItems();
    Optional<Item> selectItemById(Integer id);
    Optional<Item> selectItemByName(String name);
    void insertItem(Item item);
    void updateItem(Item item);
    boolean existsItemById(Integer id);
    boolean existsItemByName(String name);
    void deleteItemById(Integer id);
    void deleteItemByName(String name);
}

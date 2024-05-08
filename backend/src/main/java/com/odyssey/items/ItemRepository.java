package com.odyssey.items;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    boolean existsItemById(Integer id);
    boolean existsItemByName(String name);
    Optional<Item> findItemByName(String name);
    void deleteItemByName(String name);
}

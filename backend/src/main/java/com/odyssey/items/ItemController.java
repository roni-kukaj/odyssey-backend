package com.odyssey.items;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("{itemId}")
    public Item getItemById(@PathVariable("itemId") Integer itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/name/{itemName}")
    public Item getItemByName(@PathVariable("itemName") String itemName) {
        return itemService.getItemByName(itemName);
    }

    @PostMapping
    public void registerItem(@RequestBody ItemRegistrationRequest request) {
        itemService.addItem(request);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") Integer id) {
        itemService.deleteItem(id);
    }

    @PutMapping("/{itemId}")
    public void updateItem(@PathVariable("itemId") Integer itemId, @RequestBody ItemUpdateRequest request) {
        itemService.updateItem(itemId, request);
    }

}

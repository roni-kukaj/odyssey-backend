package com.odyssey.controllers;

import com.odyssey.models.Item;
import com.odyssey.dtos.ItemRegistrationRequest;
import com.odyssey.services.ItemService;
import com.odyssey.dtos.ItemUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping
    public void registerItem(@RequestBody ItemRegistrationRequest request) {
        itemService.addItem(request);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") Integer id) {
        itemService.deleteItem(id);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PutMapping("/{itemId}")
    public void updateItem(@PathVariable("itemId") Integer itemId, @RequestBody ItemUpdateRequest request) {
        itemService.updateItem(itemId, request);
    }

}

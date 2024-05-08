package com.odyssey.tripItems;

import com.odyssey.items.Item;

import java.util.Objects;

public class TripItemGetDto {
    private Integer id;
    private Item item;

    public TripItemGetDto() {}

    public TripItemGetDto(Item item) {
        this.item = item;
    }

    public TripItemGetDto(Integer id, Item item) {
        this.id = id;
        this.item = item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripItemGetDto that = (TripItemGetDto) o;
        return Objects.equals(id, that.id) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item);
    }

    @Override
    public String toString() {
        return "TripItemGetDto{" +
                "id=" + id +
                ", item=" + item +
                '}';
    }
}

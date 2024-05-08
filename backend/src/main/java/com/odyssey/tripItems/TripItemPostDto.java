package com.odyssey.tripItems;

import java.util.Objects;

public class TripItemPostDto {
    private Integer itemId;

    public TripItemPostDto() {}

    public TripItemPostDto(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripItemPostDto that = (TripItemPostDto) o;
        return Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }

    @Override
    public String toString() {
        return "TripItemPostDto{" +
                "itemId=" + itemId +
                '}';
    }
}

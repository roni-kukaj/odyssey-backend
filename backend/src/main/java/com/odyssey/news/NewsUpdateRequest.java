package com.odyssey.news;

public record NewsUpdateRequest(Integer author_id, String title, String description, String picture) {
}

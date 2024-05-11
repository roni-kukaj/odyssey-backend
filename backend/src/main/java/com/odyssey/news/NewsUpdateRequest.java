package com.odyssey.news;

public record NewsUpdateRequest(Integer authorId, String title, String description, String picture) {
}
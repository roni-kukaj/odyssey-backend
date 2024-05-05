package com.odyssey.news;

public record NewsRegistrationRequest(Integer author_id,String title, String description, String picture) {
}
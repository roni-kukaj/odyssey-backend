package com.odyssey.news;

public record NewsRegistrationRequest(Integer authorId,String title, String description, String picture) {
}
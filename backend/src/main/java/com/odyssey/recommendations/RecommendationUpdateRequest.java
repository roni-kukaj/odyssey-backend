package com.odyssey.recommendations;

public record RecommendationUpdateRequest(String description, Integer userId, Integer activityId) {
}

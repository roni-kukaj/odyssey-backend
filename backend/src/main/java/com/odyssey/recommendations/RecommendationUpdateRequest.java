package com.odyssey.recommendations;

public record RecommendationUpdateRequest(String description, Integer user_id, Integer activity_id) {
}

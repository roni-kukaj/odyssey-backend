package com.odyssey.posts;

public record PostUpdateRequest(
        String text,
        String image
) {
}

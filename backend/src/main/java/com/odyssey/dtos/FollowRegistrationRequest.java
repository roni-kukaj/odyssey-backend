package com.odyssey.dtos;

public record FollowRegistrationRequest(
        Integer followerId, Integer followingId
) {
}

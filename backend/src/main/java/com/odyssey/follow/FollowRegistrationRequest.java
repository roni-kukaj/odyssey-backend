package com.odyssey.follow;

public record FollowRegistrationRequest(
        Integer followerId, Integer followingId
) {
}

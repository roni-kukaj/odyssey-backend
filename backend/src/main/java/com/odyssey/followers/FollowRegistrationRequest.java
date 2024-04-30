package com.odyssey.followers;

public record FollowRegistrationRequest(
        Integer followerId, Integer followingId
) {
}

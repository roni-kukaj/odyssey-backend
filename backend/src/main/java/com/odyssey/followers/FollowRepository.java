package com.odyssey.followers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsFollowById(Integer id);
    boolean existsFollowByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    List<Follow> findByFollowerId(Integer followerId);
    List<Follow> findByFollowingId(Integer followingId);
    Follow findByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    void deleteByFollowerIdAndFollowingId(Integer follower, Integer following);
}

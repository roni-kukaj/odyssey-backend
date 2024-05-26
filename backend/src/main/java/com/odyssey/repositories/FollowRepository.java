package com.odyssey.repositories;

import com.odyssey.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsFollowById(Integer id);
    boolean existsFollowByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    List<Follow> findByFollowerId(Integer followerId);
    List<Follow> findByFollowingId(Integer followingId);
    Optional<Follow> findByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
}

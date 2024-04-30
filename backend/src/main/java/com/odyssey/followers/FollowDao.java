package com.odyssey.followers;

import java.util.List;
import java.util.Optional;

public interface FollowDao {
    List<Follow> selectAllFollowers();
    List<Follow> selectAllFollowingOfUserById(Integer followerId);
    List<Follow> selectAllFollowersOfUserById(Integer followingId);
    Optional<Follow> selectByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    Optional<Follow> selectById(Integer id);
    void insertFollow(Follow follow);
    boolean existsFollowByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    boolean existsFollowById(Integer id);
    void deleteFollowById(Integer id);
    void deleteFollowByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
}

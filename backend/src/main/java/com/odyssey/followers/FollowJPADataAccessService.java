package com.odyssey.followers;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("followersJPAService")
public class FollowJPADataAccessService implements FollowDao {

    private final FollowRepository followRepository;

    public FollowJPADataAccessService(FollowRepository followRepository){
        this.followRepository = followRepository;
    }


    @Override
    public List<Follow> selectAllFollowers() {
        return followRepository.findAll();
    }

    @Override
    public List<Follow> selectAllFollowingOfUserById(Integer followerId) {
        return followRepository.findByFollowerId(followerId);
    }

    @Override
    public List<Follow> selectAllFollowersOfUserById(Integer followingId) {
        return followRepository.findByFollowingId(followingId);
    }

    @Override
    public Optional<Follow> selectByFollowerIdAndFollowingId(Integer followerId, Integer followingId) {
        return Optional.ofNullable(followRepository.findByFollowerIdAndFollowingId(followerId, followingId));
    }

    @Override
    public Optional<Follow> selectById(Integer id) {
        return followRepository.findById(id);
    }

    @Override
    public void insertFollow(Follow follow) {
        followRepository.save(follow);
    }

    @Override
    public boolean existsFollowByFollowerIdAndFollowingId(Integer followerId, Integer followingId) {
        return followRepository.existsFollowByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    public boolean existsFollowById(Integer id) {
        return followRepository.existsFollowById(id);
    }

    @Override
    public void deleteFollowById(Integer id) {
        followRepository.deleteById(id);
    }

    @Override
    public void deleteFollowByFollowerIdAndFollowingId(Integer followerId, Integer followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }
}

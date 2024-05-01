package com.odyssey.follow;

import com.odyssey.user.User;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class FollowJPADataAccessServiceTest {

    private FollowJPADataAccessService followDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock private FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        followDataAccessService = new FollowJPADataAccessService(followRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllFollowers() {
        followDataAccessService.selectAllFollowers();
        verify(followRepository).findAll();
    }

    @Test
    void selectAllFollowingOfUserById() {
        int id = 1;
        followRepository.findByFollowerId(id);
        verify(followRepository).findByFollowerId(id);
    }

    @Test
    void selectAllFollowersOfUserById() {
        int id = 1;
        followDataAccessService.selectAllFollowersOfUserById(id);
        verify(followRepository).findByFollowingId(id);
    }

    @Test
    void selectByFollowerIdAndFollowingId() {
        int followerId = 1;
        int followingId = 2;
        followDataAccessService.selectByFollowerIdAndFollowingId(followerId, followingId);
        verify(followRepository).findByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Test
    void selectById() {
        int id = 1;
        followDataAccessService.selectById(id);
        verify(followRepository).findById(id);
    }

    @Test
    void insertFollow() {
        User follower = new User();
        User following = new User();
        Follow follow = new Follow(
                1,
                follower,
                following
        );
        followDataAccessService.insertFollow(follow);
        verify(followRepository).save(follow);
    }

    @Test
    void existsFollowByFollowerIdAndFollowingId() {
        int followerId = 1;
        int followingId = 2;
        followDataAccessService.existsFollowByFollowerIdAndFollowingId(followerId, followingId);
        verify(followRepository).existsFollowByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Test
    void existsFollowById() {
        int id = 1;
        followDataAccessService.existsFollowById(1);
        verify(followRepository).existsFollowById(id);
    }

    @Test
    void deleteFollowById() {
        int id = 1;
        followDataAccessService.deleteFollowById(id);
        verify(followRepository).deleteById(id);
    }

}
package com.odyssey.services;

import com.odyssey.daos.FollowDao;
import com.odyssey.dtos.FollowDeleteRequest;
import com.odyssey.dtos.FollowDto;
import com.odyssey.dtos.FollowRegistrationRequest;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Follow;
import com.odyssey.models.User;
import com.odyssey.services.utils.FollowDtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odyssey.daos.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private final FollowDao followersDao;
    private final UserDao userDao;
    private final FollowDtoMapper followDtoMapper;

    public FollowService(
            @Qualifier("followersJPAService") FollowDao followersDao,
            @Qualifier("userJPAService") UserDao userDao, FollowDtoMapper followDtoMapper
    ) {
        this.followersDao = followersDao;
        this.userDao = userDao;
        this.followDtoMapper = followDtoMapper;
    }

    public List<FollowDto> getAllFollowers() {
        return followersDao.selectAllFollowers()
                .stream()
                .map(followDtoMapper)
                .collect(Collectors.toList());
    }

    public List<FollowDto> getAllFollowingOfUserById(Integer followerId) {
        return followersDao.selectAllFollowingOfUserById(followerId)
                .stream()
                .map(followDtoMapper)
                .collect(Collectors.toList());
    }
    public List<FollowDto> getAllFollowersOfUserById(Integer followingId) {
        return followersDao.selectAllFollowersOfUserById(followingId)
                .stream()
                .map(followDtoMapper)
                .collect(Collectors.toList());
    }

    public FollowDto getFollowById(Integer id) {
        return followersDao.selectById(id)
                .map(followDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("follow record with id [%s] not found".formatted(id)));
    }

    public void addFollower(FollowRegistrationRequest request){
        User follower = userDao.selectUserById(request.followerId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.followerId())));
        User following = userDao.selectUserById(request.followingId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.followingId())));

        if (followersDao.existsFollowByFollowerIdAndFollowingId(request.followerId(), request.followingId())) {
            throw new DuplicateResourceException("record already exists");
        }

        if (!follower.getRole().getName().equals("user") || !following.getRole().getName().equals("user")) {
            throw new RequestValidationException("a user can only follow another user");
        }

        if (follower.getId().equals(following.getId())) {
            throw new RequestValidationException("a user can only follow another user");
        }

        Follow follow = new Follow(
                follower, following
        );

        followersDao.insertFollow(follow);

    }

    public void deleteFollow(Integer id) {
        if (followersDao.existsFollowById(id)) {
            followersDao.deleteFollowById(id);
        }
        else {
            throw new ResourceNotFoundException("record with id [%s] not found".formatted(id));
        }
    }

    public void deleteFollowByFollowerIdAndFollowingId(FollowDeleteRequest request){
        Follow follow = followersDao.selectByFollowerIdAndFollowingId(request.followerId(), request.followingId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] does not follow user with id [%s]".formatted(request.followerId(), request.followingId())));
        followersDao.deleteFollowById(follow.getId());
    }
}
package com.odyssey.followers;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.role.RoleDao;
import com.odyssey.user.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odyssey.user.UserDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {

    private final FollowDao followersDao;
    private final UserDao userDao;
    private final RoleDao roleDao;

    public FollowService(
            @Qualifier("followersJPAService") FollowDao followersDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("roleJPAService") RoleDao roleDao
            ) {
        this.followersDao = followersDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public List<Follow> getAllFollowers() {
        return followersDao.selectAllFollowers();
    }

    public List<Follow> getAllFollowingOfUserById(Integer followerId) {
        return followersDao.selectAllFollowingOfUserById(followerId);
    }
    public List<Follow> getAllFollowersOfUserById(Integer followingId) {
        return followersDao.selectAllFollowersOfUserById(followingId);
    }

    public Follow getFollowersById(Integer id) {
        return followersDao.selectById(id)
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

        if (!follower.getRole().getName().equals("user") && !following.getRole().getName().equals("user")) {
            throw new RequestValidationException("a user can follow only another user");
        }

        Follow follow = new Follow(
                follower, following
        );

        followersDao.insertFollow(follow);

    }

    public boolean deleteFollow(Integer id) {
        if (followersDao.existsFollowById(id)) {
            followersDao.deleteFollowById(id);
        }
        else {
            throw new ResourceNotFoundException("record with id [%s] not fount".formatted(id));
        }
        return false;
    }

    public boolean deleteFollowByFollowerIdAndFollowingId(FollowDeleteRequest request){
        Follow follow = followersDao.selectByFollowerIdAndFollowingId(request.followerId(), request.followingId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] does not follow user with id [%s]".formatted(request.followerId(), request.followingId())));
        followersDao.deleteFollowById(follow.getId());

        return false;
    }

}

package com.odyssey.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    @Autowired
    private FollowService followersService;

    @GetMapping
    public List<Follow> getFollowers() {
        return followersService.getAllFollowers();
    }

    @GetMapping("/{followerId}")
    public List<Follow> getFollowingByFollowerId(@PathVariable("followerId") Integer followerId) {
        return followersService.getAllFollowingOfUserById(followerId);
    }

    @GetMapping("/{userId}/followers")
    public List<Follow> getFollowersOfUserById(@PathVariable("userId") Integer userId) {
        return followersService.getAllFollowersOfUserById(userId);
    }

    @GetMapping("/record/{recordId}")
    public Follow getFollowById(@PathVariable("recordId") Integer recordId) {
        return followersService.getFollowById(recordId);
    }

    @PostMapping()
    public void registerFollowerRecord(@RequestBody FollowRegistrationRequest request) {
        followersService.addFollower(request);
    }

    @DeleteMapping("/{followerId}/{followingId}/delete")
    public void deleteFollowerRecord(@PathVariable Integer followerId, @PathVariable Integer followingId){
        followersService.deleteFollowByFollowerIdAndFollowingId(new FollowDeleteRequest(followerId, followingId));
    }

    @DeleteMapping("/{recordId}")
    public void deleteFollowerRecordById(@PathVariable("recordId") Integer recordId) {
        followersService.deleteFollow(recordId);
    }

}

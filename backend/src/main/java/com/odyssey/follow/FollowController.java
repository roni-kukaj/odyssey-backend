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

    @PostMapping()
    public void registerFollowerRecord(@RequestBody FollowRegistrationRequest request) {
        followersService.addFollower(request);
    }

    @DeleteMapping()
    public void deleteFollowerRecord(@RequestBody FollowDeleteRequest request){
        followersService.deleteFollowByFollowerIdAndFollowingId(request);
    }

    @DeleteMapping("/{recordId}")
    public void deleteFollowerRecordById(@PathVariable("recordId") Integer recordId) {
        followersService.deleteFollow(recordId);
    }

}

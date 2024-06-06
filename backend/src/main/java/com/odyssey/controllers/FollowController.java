package com.odyssey.controllers;

import com.odyssey.dtos.FollowDeleteRequest;
import com.odyssey.dtos.FollowDto;
import com.odyssey.dtos.FollowRegistrationRequest;
import com.odyssey.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    @Autowired
    private FollowService followersService;

    @GetMapping
    public List<FollowDto> getFollowers() {
        return followersService.getAllFollowers();
    }

    @GetMapping("/{followerId}/following")
    public List<FollowDto> getFollowingByFollowerId(@PathVariable("followerId") Integer followerId) {
        return followersService.getAllFollowingOfUserById(followerId);
    }

    @GetMapping("/{userId}/followers")
    public List<FollowDto> getFollowersOfUserById(@PathVariable("userId") Integer userId) {
        return followersService.getAllFollowersOfUserById(userId);
    }

    @GetMapping("/{recordId}")
    public FollowDto getFollowById(@PathVariable("recordId") Integer recordId) {
        return followersService.getFollowById(recordId);
    }

    @PreAuthorize("hasAuthority('USER') and #request.followerId == authentication.principal.id")
    @PostMapping()
    public void registerFollowerRecord(@RequestBody FollowRegistrationRequest request) {
        followersService.addFollower(request);
    }

    @PreAuthorize("hasAuthority('USER') and #followerId == authentication.principal.id")
    @DeleteMapping("/{followerId}/{followingId}/delete")
    public void deleteFollowerRecord(@PathVariable Integer followerId, @PathVariable Integer followingId){
        followersService.deleteFollowByFollowerIdAndFollowingId(new FollowDeleteRequest(followerId, followingId));
    }

    @DeleteMapping("/{recordId}")
    public void deleteFollowerRecordById(@PathVariable("recordId") Integer recordId) {
        followersService.deleteFollow(recordId);
    }

}

package com.odyssey.controllers;

import com.odyssey.models.Subscriber;
import com.odyssey.dtos.SubscriberRegistrationRequest;
import com.odyssey.services.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PreAuthorize("hasAuthority('MAINADMIN') or hasAuthority('ADMIN')")
    @GetMapping
    public List<Subscriber> getAllSubscribers(){
        return subscriberService.getAllSubscribers();
    }

    @PreAuthorize("hasAuthority('MAINADMIN') or hasAuthority('ADMIN')")
    @GetMapping("/{subscriberId}")
    public Subscriber getSubscriberById(@PathVariable("subscriberId") Integer subscriberId){
        return subscriberService.getSubscriber(subscriberId);
    }

    @DeleteMapping("/{subscriberId}")
    public void deleteSubscriber(@PathVariable("subscriberId") Integer subscriberId){
        subscriberService.deleteSubscriber(subscriberId);
    }

    @PostMapping
    public void registerSubscriber(@RequestBody SubscriberRegistrationRequest request) {
        subscriberService.addSubscriber(request);
    }
}

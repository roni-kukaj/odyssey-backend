package com.odyssey.subscribers;

import com.odyssey.role.RoleRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping
    public List<Subscriber> getAllSubscribers(){
        return subscriberService.getAllSubscribers();
    }

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

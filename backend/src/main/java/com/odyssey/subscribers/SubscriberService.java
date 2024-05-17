package com.odyssey.subscribers;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {

    private final SubscriberDao subscriberDao;

    public SubscriberService(@Qualifier("subscriberJPAService") SubscriberDao subscriberDao){
        this.subscriberDao = subscriberDao;
    }

    public List<Subscriber> getAllSubscribers(){
        return subscriberDao.selectAllSubscribers();
    }

    public Subscriber getSubscriber(Integer id){
        return subscriberDao.selectSubscriberById(id)
                .orElseThrow(() -> new ResourceNotFoundException("subscriber with id [%s] not found".formatted(id)));
    }

    public void addSubscriber(SubscriberRegistrationRequest request){
        if(subscriberDao.existsSubscriberByEmail(request.email())){
            throw new DuplicateResourceException("subscriber already exists");
        }
        Subscriber subscriber = new Subscriber(
                request.email()
        );
        subscriberDao.insertSubscriber(subscriber);
    }

    public void deleteSubscriber(Integer id){
        if (subscriberDao.existsSubscriberById(id)){
            subscriberDao.deleteSubscriberById(id);
        }
        else {
            throw new ResourceNotFoundException("subscriber with id [%s] not found".formatted(id));
        }
    }

}

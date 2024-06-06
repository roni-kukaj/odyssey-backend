package com.odyssey.services.data;

import com.odyssey.daos.SubscriberDao;
import com.odyssey.models.Subscriber;
import com.odyssey.repositories.SubscriberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("subscriberJPAService")
public class SubscriberJPADataAccessService implements SubscriberDao {

    private final SubscriberRepository subscriberRepository;

    public SubscriberJPADataAccessService(SubscriberRepository subscriberRepository){
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public List<Subscriber> selectAllSubscribers() {
        return subscriberRepository.findAll();
    }

    @Override
    public Optional<Subscriber> selectSubscriberById(Integer id) {
        return subscriberRepository.findById(id);
    }

    @Override
    public void insertSubscriber(Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }

    @Override
    public boolean existsSubscriberById(Integer id) {
        return subscriberRepository.existsSubscriberById(id);
    }

    @Override
    public boolean existsSubscriberByEmail(String email) {
        return subscriberRepository.existsSubscriberByEmail(email);
    }


    @Override
    public void deleteSubscriberById(Integer id) {
        subscriberRepository.deleteById(id);
    }
}

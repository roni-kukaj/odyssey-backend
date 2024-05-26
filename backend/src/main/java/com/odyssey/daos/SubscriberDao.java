package com.odyssey.daos;

import com.odyssey.models.Subscriber;

import java.util.List;
import java.util.Optional;

public interface SubscriberDao {
    List<Subscriber> selectAllSubscribers();
    Optional<Subscriber> selectSubscriberById(Integer id);
    void insertSubscriber(Subscriber subscriber);
    boolean existsSubscriberById(Integer id);
    boolean existsSubscriberByEmail(String email);
    void deleteSubscriberById(Integer id);
}

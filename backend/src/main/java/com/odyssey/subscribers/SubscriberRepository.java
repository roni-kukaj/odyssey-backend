package com.odyssey.subscribers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    boolean existsSubscriberById(Integer id);
    boolean existsSubscriberByEmail(String email);
}

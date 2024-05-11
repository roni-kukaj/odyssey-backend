package com.odyssey.subscribers;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class SubscriberJPADataAccessServiceTest {
    private SubscriberJPADataAccessService subscriberDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private SubscriberRepository subscriberRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        subscriberDataAccessService = new SubscriberJPADataAccessService(subscriberRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllSubscribers() {
        subscriberDataAccessService.selectAllSubscribers();
        verify(subscriberRepository).findAll();
    }

    @Test
    void selectSubscriberById() {
        int id = 1;
        subscriberDataAccessService.selectSubscriberById(id);
        verify(subscriberRepository).findById(id);
    }

    @Test
    void insertSubscriber() {
        Subscriber subscriber = new Subscriber(1, "user");
        subscriberDataAccessService.insertSubscriber(subscriber);
        verify(subscriberRepository).save(subscriber);
    }

    @Test
    void existsSubscriberById() {
        int id = 1;
        subscriberDataAccessService.existsSubscriberById(id);
        verify(subscriberRepository).existsSubscriberById(id);
    }

    @Test
    void existsSubscriberByEmail() {
        String email = "admin@gmail.com";
        subscriberDataAccessService.existsSubscriberByEmail(email);
        verify(subscriberRepository).existsSubscriberByEmail(email);
    }

    @Test
    void deleteSubscriberById() {
        int id = 1;
        subscriberDataAccessService.deleteSubscriberById(id);
        verify(subscriberRepository).deleteById(id);
    }
}

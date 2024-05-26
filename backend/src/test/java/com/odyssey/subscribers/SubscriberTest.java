package com.odyssey.subscribers;

import com.odyssey.models.Subscriber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubscriberTest {

    @Test
    void testSubscriberGettersAndSetters(){
        Subscriber subscriber = new Subscriber();
        subscriber.setId(1);
        subscriber.setEmail("admin@gmail.com");

        assertEquals(1, subscriber.getId());
        assertEquals("admin@gmail.com", subscriber.getEmail());
    }
}

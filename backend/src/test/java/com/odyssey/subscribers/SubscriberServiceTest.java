package com.odyssey.subscribers;

import com.odyssey.daos.SubscriberDao;
import com.odyssey.dtos.SubscriberRegistrationRequest;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Subscriber;
import com.odyssey.services.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriberServiceTest {

    @Mock
    private SubscriberDao subscriberDao;
    private SubscriberService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SubscriberService(subscriberDao);
    }

    @Test
    void getAllSubscribers() {
        // When
        underTest.getAllSubscribers();

        // Then
        verify(subscriberDao).selectAllSubscribers();
    }

    @Test
    void canGetSubscriber() {
        // Given
        int id = 1;
        Subscriber subscriber = new Subscriber(1, "user");

        when(subscriberDao.selectSubscriberById(id)).thenReturn(Optional.of(subscriber));

        // When
        Subscriber actual = underTest.getSubscriber(id);

        // Then
        assertThat(actual).isEqualTo(subscriber);
    }

    @Test
    void willThrowWhenGetSubscriberReturnEmptyOptional() {
        // Given
        int id = 1;
        when(subscriberDao.selectSubscriberById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getSubscriber(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("subscriber with id [%s] not found".formatted(id));
    }

    @Test
    void addSubscriber() {
        // Given
        String email = "admin@gmail.com";
        when(subscriberDao.existsSubscriberByEmail(email)).thenReturn(false);

        SubscriberRegistrationRequest request = new SubscriberRegistrationRequest(email);

        // When
        underTest.addSubscriber(request);

        // Then
        ArgumentCaptor<Subscriber> subscriberArgumentCaptor = ArgumentCaptor.forClass(Subscriber.class);
        verify(subscriberDao).insertSubscriber(subscriberArgumentCaptor.capture());

        Subscriber capturedSubscriber = subscriberArgumentCaptor.getValue();

        assertThat(capturedSubscriber.getId()).isNull();
        assertThat(capturedSubscriber.getEmail()).isEqualTo(request.email());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingSubscriber() {
        // Given
        String email = "user";

        when(subscriberDao.existsSubscriberByEmail(email)).thenReturn(true);

        SubscriberRegistrationRequest request = new SubscriberRegistrationRequest(email);

        // When
        assertThatThrownBy(() -> underTest.addSubscriber(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("subscriber already exists");

        // Then
        verify(subscriberDao, never()).insertSubscriber(any());
    }

    @Test
    void willThrowDeleteSubscriberByIdNotExists() {
        // Given
        int id = 10;
        when(subscriberDao.existsSubscriberById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteSubscriber(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("subscriber with id [%s] not found".formatted(id));

        // Then
        verify(subscriberDao, never()).deleteSubscriberById(any());
    }
}

package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.odyssey.subscribers.Subscriber;
import com.odyssey.subscribers.SubscriberRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubscriberIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String SUBSCRIBER_URI = "/api/v1/subscribers";

    @Test
    void canRegisterASubscriber() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String email = faker.internet().emailAddress();

        SubscriberRegistrationRequest request = new SubscriberRegistrationRequest(email);

        webTestClient.post()
                .uri(SUBSCRIBER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), SubscriberRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Subscriber> allSubscribers = webTestClient.get()
                .uri(SUBSCRIBER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Subscriber>() {})
                .returnResult()
                .getResponseBody();

        Subscriber expectedSubscriber = new Subscriber(
                email
        );

        assertThat(allSubscribers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedSubscriber);

        int id = allSubscribers.stream()
                .filter(subscriber -> subscriber.getEmail().equals(email))
                .map(Subscriber::getId)
                .findFirst()
                .orElseThrow();

        expectedSubscriber.setId(id);

        webTestClient.get()
                .uri(SUBSCRIBER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Subscriber>() {})
                .isEqualTo(expectedSubscriber);
    }

    @Test
    void canDeleteSubscriber() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String email = faker.internet().emailAddress();

        SubscriberRegistrationRequest request = new SubscriberRegistrationRequest(email);

        webTestClient.post()
                .uri(SUBSCRIBER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), SubscriberRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Subscriber> allSubscribers = webTestClient.get()
                .uri(SUBSCRIBER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Subscriber>() {})
                .returnResult()
                .getResponseBody();

        Subscriber expectedSubscriber = new Subscriber(
                email
        );

        assertThat(allSubscribers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedSubscriber);

        int id = allSubscribers.stream()
                .filter(subscriber -> subscriber.getEmail().equals(email))
                .map(Subscriber::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(SUBSCRIBER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(SUBSCRIBER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}

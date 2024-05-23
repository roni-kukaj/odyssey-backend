package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.odyssey.role.Role;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripRegistrationRequest;
import com.odyssey.trips.TripUpdateRequest;
import com.odyssey.user.User;
import com.odyssey.user.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TripIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String TRIP_URI = "/api/v1/trips";
    private static final String USER_URI = "/api/v1/users";

    private User setUpUser() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String username = name;
        String email = name + "@gmail.com";
        String password = "passi";
        String avatar = "avatar";
        Role role = new Role(1, "user");
        UserRegistrationRequest request = new UserRegistrationRequest(
                name, username, email, password, avatar, role.getId()
        );
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        List<User> allUsers = webTestClient.get()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {})
                .returnResult()
                .getResponseBody();

        int id = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        return new User(id, name, username, email, password, avatar, role);
    }

    @Test
    void canRegisterATrip() {
        User user = setUpUser();
        LocalDate localDate = LocalDate.now();
        TripRegistrationRequest request = new TripRegistrationRequest(
                user.getId(), localDate, localDate,
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        webTestClient.post()
                .uri(TRIP_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), TripRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Trip> allTrips = webTestClient.get()
                .uri(TRIP_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Trip>() {})
                .returnResult()
                .getResponseBody();

        Trip expectedTrip = new Trip(
                user, localDate, localDate,
                Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet()
        );

        assertThat(allTrips)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedTrip);

        int id = allTrips.stream()
                .filter(trip -> trip.getUser().equals(user) && trip.getStartDate().equals(localDate) && trip.getEndDate().equals(localDate))
                .map(Trip::getId)
                .findFirst()
                .orElseThrow();

        expectedTrip.setId(id);

        webTestClient.get()
                .uri(TRIP_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Trip>() {})
                .isEqualTo(expectedTrip);
    }

    @Test
    void canDeleteTrip() {
        User user = setUpUser();
        LocalDate localDate = LocalDate.now();
        TripRegistrationRequest request = new TripRegistrationRequest(
                user.getId(), localDate, localDate,
                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
        );
        webTestClient.post()
                .uri(TRIP_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), TripRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Trip> allTrips = webTestClient.get()
                .uri(TRIP_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Trip>() {})
                .returnResult()
                .getResponseBody();

        Trip expectedTrip = new Trip(
                user, localDate, localDate,
                Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet()
        );

        assertThat(allTrips)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedTrip);

        int id = allTrips.stream()
                .filter(trip -> trip.getUser().equals(user) && trip.getStartDate().equals(localDate) && trip.getEndDate().equals(localDate))
                .map(Trip::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(TRIP_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(TRIP_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateTripAllFields() {
//        User user = setUpUser();
//        LocalDate localDate = LocalDate.now();
//        TripRegistrationRequest request = new TripRegistrationRequest(
//                user.getId(), localDate, localDate,
//                Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()
//        );
//        webTestClient.post()
//                .uri(TRIP_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), TripRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        List<Trip> allTrips = webTestClient.get()
//                .uri(TRIP_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Trip>() {})
//                .returnResult()
//                .getResponseBody();
//
//        int id = allTrips.stream()
//                .filter(trip -> trip.getUser().equals(user) && trip.getStartDate().equals(localDate) && trip.getEndDate().equals(localDate))
//                .map(Trip::getId)
//                .findFirst()
//                .orElseThrow();
//
//        TripUpdateRequest updateRequest = new TripUpdateRequest(
//                user.getId(), LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 1), null, null, null, null
//        );
//
//        webTestClient.put()
//                .uri(TRIP_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(updateRequest), TripUpdateRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        Trip updatedTrip = webTestClient.get()
//                .uri(TRIP_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(Trip.class)
//                .returnResult()
//                .getResponseBody();
//
//        Trip expected = new Trip(
//                id, user, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 1), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet()
//        );
//        assertThat(updatedTrip).isEqualTo(expected);
    }

}

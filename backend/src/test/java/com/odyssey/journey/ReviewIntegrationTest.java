package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityRegistrationRequest;
import com.odyssey.activities.ActivityUpdateRequest;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationRequest;
import com.odyssey.reviews.Review;
import com.odyssey.reviews.ReviewRegistrationRequest;
import com.odyssey.reviews.ReviewUpdateRequest;
import com.odyssey.role.Role;
import com.odyssey.user.User;
import com.odyssey.user.UserRegistrationRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String REVIEW_URI = "/api/v1/reviews";
    private static final String LOCATION_URI = "/api/v1/locations";
    private static final String USER_URI = "/api/v1/users";

    private User setUpUser(){
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String username = name;
        String email = faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "1234";
        String avatar = "avatar";
        Role role = new Role();
        int roleId = 1;
        role.setId(roleId);
        role.setName("user");

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                name, username, email, password, avatar, roleId
        );

        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
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
                .filter(user -> user.getFullname().equals(name) && user.getUsername().equals(username)
                        && user.getEmail().equals(email) && user.getPassword().equals(password) )
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        return new User(id, name, username, email, password, avatar, role);
    }

    private Location setUpLocation() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = city;
        String picture = "pic";
        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city,
                country,
                picture
        );
        webTestClient.post()
                .uri(LOCATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LocationRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        List<Location> allLocations = webTestClient.get()
                .uri(LOCATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Location>() {})
                .returnResult()
                .getResponseBody();

        int id = allLocations.stream()
                .filter(location -> location.getCity().equals(city) && location.getCountry().equals(country))
                .map(Location::getId)
                .findFirst()
                .orElseThrow();
        return new Location(id, city, country, picture);
    }

    @Test
    void canAddAReview() {
        Faker faker = new Faker();
        String description = faker.name().fullName();
        Integer rating = RANDOM.nextInt(1,5);
        User user = setUpUser();
        Location location = setUpLocation();

        ReviewRegistrationRequest reviewRegistrationRequest = new ReviewRegistrationRequest(
                description, rating, user.getId(), location.getId()
        );

        webTestClient.post()
                .uri(REVIEW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(reviewRegistrationRequest), ReviewRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();

        List<Review> allReviews = webTestClient.get()
                .uri(REVIEW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Review>() {})
                .returnResult().getResponseBody();

        Review expectedReview = new Review(description,rating,user,location);
        Assertions.assertThat(allReviews)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedReview);

        int id = allReviews.stream()
                .filter(review -> review.getDescription().equals(description) && review.getRating().equals(rating)
                )
                .map(Review::getId)
                .findFirst().orElseThrow();
        expectedReview.setId(id);

        webTestClient.get()
                .uri(REVIEW_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Review>() {})
                .isEqualTo(expectedReview);
    }

    @Test
    void canDeleteReview() {
        Faker faker = new Faker();
        String desc = faker.name().fullName();
        Integer rating = RANDOM.nextInt(1, 5);
        User user = setUpUser();
        Location location = setUpLocation();

        ReviewRegistrationRequest request = new ReviewRegistrationRequest(
                 desc, rating, user.getId(), location.getId()
        );
        webTestClient.post()
                .uri(REVIEW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ReviewRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Review> allReviews = webTestClient.get()
                .uri(REVIEW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Review>() {})
                .returnResult()
                .getResponseBody();

        Review expectedReview= new Review(
                 desc, rating, user, location
        );

        Assertions.assertThat(allReviews)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedReview);

        int id = allReviews.stream()
                .filter(review -> review.getDescription().equals(desc) && review.getRating().equals(rating))
                .map(Review::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(REVIEW_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(REVIEW_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }


    @Test
    void canUpdateReview() {
        Faker faker = new Faker();
        String desc = faker.name().fullName();
        Integer rating = RANDOM.nextInt(1, 5);
        User user = setUpUser();
        Location location = setUpLocation();

        ReviewRegistrationRequest request = new ReviewRegistrationRequest(
                desc, rating, user.getId(), location.getId()
        );
        webTestClient.post()
                .uri(REVIEW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ReviewRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Review> allReviews = webTestClient.get()
                .uri(REVIEW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Review>() {})
                .returnResult()
                .getResponseBody();

        int id = allReviews.stream()
                .filter(review -> review.getDescription().equals(desc) && review.getRating().equals(rating))
                .map(Review::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        String desc2 = faker2.name().fullName();
        Integer rating2 = RANDOM.nextInt(1, 5);
        User user2 = setUpUser();
        Location location2 = setUpLocation();
        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest(
               desc2, rating2, user2.getId(), location2.getId()
        );

        webTestClient.put()
                .uri(REVIEW_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), ReviewUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Review updatedReview = webTestClient.get()
                .uri(REVIEW_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Review.class)
                .returnResult()
                .getResponseBody();

        Review expected = new Review(
                id, desc2, rating2, user2, location2
        );
        Assertions.assertThat(updatedReview).isEqualTo(expected);
    }
}

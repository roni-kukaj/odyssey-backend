package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.odyssey.posts.Post;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripRegistrationRequest;
import com.odyssey.user.User;
import com.odyssey.role.Role;
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
public class PostIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String POST_URI = "/api/v1/posts";
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
                name, username, email, password
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

    private Trip setUpTrip(User user) {
        LocalDate localDate = LocalDate.now();
        TripRegistrationRequest request = new TripRegistrationRequest(
                user.getId(),
                localDate,
                localDate,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
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

        int id = allTrips.stream()
                .filter(trip -> trip.getStartDate().equals(localDate) && trip.getEndDate().equals(localDate) && trip.getUser().getId().equals(user.getId()))
                .map(Trip::getId)
                .findFirst()
                .orElseThrow();
        return new Trip(id, user, localDate, localDate, Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Test
    void canRegisterAPost() {
        User user = setUpUser();
        Trip trip = setUpTrip(user);

//        PostRegistrationRequest request = new PostRegistrationRequest(
//                "Post", "Image", user.getId(), trip.getId()
//        );
//
//        webTestClient.post()
//                .uri(POST_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), PostRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

        List<Post> allPosts = webTestClient.get()
                .uri(POST_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Post>() {})
                .returnResult()
                .getResponseBody();

        Post expectedPost = new Post(
                "Post", "Image", trip.getStartDate(), user, trip
        );

        assertThat(allPosts)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPost);

        int id = allPosts.stream()
                .filter(post -> post.getUser().equals(user) && post.getTrip().equals(trip))
                .map(Post::getId)
                .findFirst()
                .orElseThrow();

        expectedPost.setId(id);

        webTestClient.get()
                .uri(POST_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Post>() {})
                .isEqualTo(expectedPost);
    }

    @Test
    void canDeletePostById() {
        User user = setUpUser();
        Trip trip = setUpTrip(user);

//        PostRegistrationRequest request = new PostRegistrationRequest(
//                "Post", "Image", user.getId(), trip.getId()
//        );
//
//        webTestClient.post()
//                .uri(POST_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), PostRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

        List<Post> allPosts = webTestClient.get()
                .uri(POST_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Post>() {})
                .returnResult()
                .getResponseBody();

        Post expectedPost = new Post(
                "Post", "Image", trip.getStartDate(), user, trip
        );

        assertThat(allPosts)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPost);

        int id = allPosts.stream()
                .filter(post -> post.getUser().equals(user) && post.getTrip().equals(trip))
                .map(Post::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(POST_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(POST_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canDeletePostsByUserId() {
        User user = setUpUser();
        Trip trip = setUpTrip(user);

//        PostRegistrationRequest request = new PostRegistrationRequest(
//                "Post", "Image", user.getId(), trip.getId()
//        );
//
//        webTestClient.post()
//                .uri(POST_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), PostRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

        List<Post> allPosts = webTestClient.get()
                .uri(POST_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Post>() {})
                .returnResult()
                .getResponseBody();

        Post expectedPost = new Post(
                "Post", "Image", trip.getStartDate(), user, trip
        );

        assertThat(allPosts)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPost);

        int id = allPosts.stream()
                .filter(post -> post.getUser().equals(user) && post.getTrip().equals(trip))
                .map(Post::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(POST_URI + "/user/{id}", user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(POST_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canUpdatePostAllField() {
        User user = setUpUser();
        Trip trip = setUpTrip(user);

//        PostRegistrationRequest request = new PostRegistrationRequest(
//                "Post", "Image", user.getId(), trip.getId()
//        );
//
//        webTestClient.post()
//                .uri(POST_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), PostRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

        List<Post> allPosts = webTestClient.get()
                .uri(POST_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Post>() {})
                .returnResult()
                .getResponseBody();

        int id = allPosts.stream()
                .filter(post -> post.getUser().equals(user) && post.getTrip().equals(trip))
                .map(Post::getId)
                .findFirst()
                .orElseThrow();

        String newText = "new text";
        String newImage = "new image";
//        PostUpdateRequest updateRequest = new PostUpdateRequest(
//                newText, newImage
//        );
//
//        webTestClient.put()
//                .uri(POST_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(updateRequest), PostUpdateRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

        Post updatedPost = webTestClient.get()
                .uri(POST_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Post.class)
                .returnResult()
                .getResponseBody();

        Post expected = new Post(
                id, newText, newImage, trip.getStartDate(), user, trip
        );
        assertThat(updatedPost).isEqualTo(expected);

    }

}

package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.odyssey.follow.Follow;
import com.odyssey.follow.FollowDao;
import com.odyssey.follow.FollowDeleteRequest;
import com.odyssey.follow.FollowRegistrationRequest;
import com.odyssey.role.Role;
import com.odyssey.user.User;
import com.odyssey.user.UserRegistrationRequest;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FollowIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String FOLLOW_URI = "api/v1/follow";
    private static final String USER_URI = "api/v1/users";

    @Autowired
    private FollowDao followDao;

    @Test
    void canRegisterAFollow() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String username = name;
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar1";
        Role role = new Role(1, "user");

        UserRegistrationRequest request1 = new UserRegistrationRequest(
                name, username, email, password
        );
        Faker faker2 = new Faker();
        Name fakerName2 = faker2.name();
        String name2 = fakerName2.fullName();
        String username2 = name2;
        String email2 = fakerName2.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password2 = "123";
        String avatar2 = "avatar1";
        UserRegistrationRequest request2 = new UserRegistrationRequest(
                name2, username2, email2, password2
        );
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request1), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), UserRegistrationRequest.class)
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

        User follower = new User(
                name, username, email, password, avatar, role
        );
        User following = new User(
                name2, username2, email2, password2, avatar2, role
        );
        int followerId = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        follower.setId(followerId);
        int followingId = allUsers.stream()
                .filter(user -> user.getEmail().equals(email2))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        following.setId(followingId);

        // Now we have both users. We make one follow the other
        FollowRegistrationRequest request = new FollowRegistrationRequest(
                followerId, followingId
        );
        webTestClient.post()
                .uri(FOLLOW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FollowRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Follow> allFollows = webTestClient.get()
                .uri(FOLLOW_URI)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Follow>(){})
                .returnResult()
                .getResponseBody();

        Follow expectedFollow = new Follow(
                follower, following
        );

        assertThat(allFollows)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedFollow);
        
        int id = allFollows.stream()
                .filter(follow -> follow.getFollower().getEmail().equals(email) && follow.getFollowing().getEmail().equals(email2))
                .map(Follow::getId)
                .findFirst()
                .orElseThrow();
        expectedFollow.setId(id);
        
        webTestClient.get()
                .uri(FOLLOW_URI + "/record/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Follow>() {})
                .isEqualTo(expectedFollow);
    }

    @Test
    void canDeleteFollowByRecordId() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String username = name;
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar1";
        Role role = new Role(1, "user");

        UserRegistrationRequest request1 = new UserRegistrationRequest(
                name, username, email, password
        );
        Faker faker2 = new Faker();
        Name fakerName2 = faker2.name();
        String name2 = fakerName2.fullName();
        String username2 = name2;
        String email2 = fakerName2.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password2 = "123";
        String avatar2 = "avatar1";
        UserRegistrationRequest request2 = new UserRegistrationRequest(
                name2, username2, email2, password2
        );
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request1), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), UserRegistrationRequest.class)
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

        User follower = new User(
                name, username, email, password, avatar, role
        );
        User following = new User(
                name2, username2, email2, password2, avatar2, role
        );
        int followerId = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        follower.setId(followerId);
        int followingId = allUsers.stream()
                .filter(user -> user.getEmail().equals(email2))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        following.setId(followingId);

        // Now we have both users. We make one follow the other
        FollowRegistrationRequest request = new FollowRegistrationRequest(
                followerId, followingId
        );
        webTestClient.post()
                .uri(FOLLOW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FollowRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Follow> allFollows = webTestClient.get()
                .uri(FOLLOW_URI)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Follow>(){})
                .returnResult()
                .getResponseBody();

        Follow expectedFollow = new Follow(
                follower, following
        );

        assertThat(allFollows)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedFollow);

        int id = allFollows.stream()
                .filter(follow -> follow.getFollower().getEmail().equals(email) && follow.getFollowing().getEmail().equals(email2))
                .map(Follow::getId)
                .findFirst()
                .orElseThrow();
        expectedFollow.setId(id);

        webTestClient.delete()
                .uri(FOLLOW_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(FOLLOW_URI + "/record/{recordId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canDeleteFollowByFollowerIdAndFollowingId() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String username = name;
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar1";
        Role role = new Role(1, "user");

        UserRegistrationRequest request1 = new UserRegistrationRequest(
                name, username, email, password
        );
        Faker faker2 = new Faker();
        Name fakerName2 = faker2.name();
        String name2 = fakerName2.fullName();
        String username2 = name2;
        String email2 = fakerName2.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password2 = "123";
        String avatar2 = "avatar1";
        UserRegistrationRequest request2 = new UserRegistrationRequest(
                name2, username2, email2, password2
        );
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request1), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), UserRegistrationRequest.class)
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

        User follower = new User(
                name, username, email, password, avatar, role
        );
        User following = new User(
                name2, username2, email2, password2, avatar2, role
        );
        int followerId = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        follower.setId(followerId);
        int followingId = allUsers.stream()
                .filter(user -> user.getEmail().equals(email2))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        following.setId(followingId);

        // Now we have both users. We make one follow the other
        FollowRegistrationRequest request = new FollowRegistrationRequest(
                followerId, followingId
        );
        webTestClient.post()
                .uri(FOLLOW_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), FollowRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Follow> allFollows = webTestClient.get()
                .uri(FOLLOW_URI)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Follow>(){})
                .returnResult()
                .getResponseBody();

        Follow expectedFollow = new Follow(
                follower, following
        );

        assertThat(allFollows)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedFollow);

        int id = allFollows.stream()
                .filter(follow -> follow.getFollower().getEmail().equals(email) && follow.getFollowing().getEmail().equals(email2))
                .map(Follow::getId)
                .findFirst()
                .orElseThrow();
        expectedFollow.setId(id);

        FollowDeleteRequest deleteRequest = new FollowDeleteRequest(
                followerId, followingId
        );

        webTestClient.delete()
                .uri(FOLLOW_URI + "/{followerId}/{followingId}/delete", followerId, followingId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(FOLLOW_URI + "/record/{recordId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }


}

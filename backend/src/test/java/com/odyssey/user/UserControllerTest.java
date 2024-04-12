package com.odyssey.user;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
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
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient; // this will be out postman

    private static final Random RANDOM = new Random();
    private static final String USER_URI = "/api/v1/users";

    // When it comes to API testing, never invoke the controller directly
    // because we don't want to call the methods, but we want to send requests

    @Test
    void canRegisterAUser(Integer id,String fullname, String username, String password, String location) {
        // Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1, 100);

        UserRegistrationRequest request = new UserRegistrationRequest(
                fullname, username, email, password, location
        );

        // send a post request
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<User> allUsers = webTestClient.get()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {})
                .returnResult()
                .getResponseBody();

        // make sure that the user is present
        User expectedUser = new User(
                 id,  fullname, username, email, password, location
        );

        assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedUser);

        // get user by id

        id = allUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        expectedUser.setId(id);

        webTestClient.get()
                .uri(USER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<User>() {})
                .isEqualTo(expectedUser);
    }

    @Test
    void canDeleteUser(
            Integer id, String fullname, String username, String password, String location) {
        // Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1, 100);

        UserRegistrationRequest request = new UserRegistrationRequest(
                fullname, username, email, password, location
        );

        // send a post request
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<User> allUsers = webTestClient.get()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {})
                .returnResult()
                .getResponseBody();

        // make sure that the user is present
        User expectedUser = new User(
                 id, username, email, password, location
        );

        assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedUser);

        // get customer by id

        id = allUsers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(User::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(USER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(USER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateUser(String fullname, String username, String email, String password, String location) {
        // Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        int age = RANDOM.nextInt(1, 100);

        UserRegistrationRequest request = new UserRegistrationRequest(
                 fullname, username, email, password, location
        );

        // send a post request
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<User> allUsers = webTestClient.get()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {})
                .returnResult()
                .getResponseBody();

        String finalEmail = email;
        int id = allUsers.stream()
                .filter(user -> user.getEmail().equals(finalEmail))
                .map(User::getId)
                .findFirst()
                .orElseThrow();

        // update user
        String newName = "Ali";
        UserUpdateRequest updateRequest = new UserUpdateRequest(
                fullname, username, email, password, location
        );

        webTestClient.put()
                .uri(USER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), UserUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get user by id
        User updatedUser = webTestClient.get()
                .uri(USER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();

        User expected = new User(
                id, username, email, password, location
        );
        assertThat(updatedUser).isEqualTo(expected);
    }

}

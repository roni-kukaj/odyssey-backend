package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.odyssey.role.Role;
import com.odyssey.role.RoleDao;
import com.odyssey.role.RoleRegistrationRequest;
import com.odyssey.role.RoleRepository;
import com.odyssey.user.User;
import com.odyssey.user.UserRegistrationRequest;
import com.odyssey.user.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.publisher.Mono.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String USER_URI = "/api/v1/users";

    @Autowired
    private RoleDao roleDao;

    @Test
    void canRegisterAUser() {
        // Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String username = name;
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar1";
        Role role = new Role(1, "user");

        UserRegistrationRequest request = new UserRegistrationRequest(
            name, username, email, password, avatar, 1
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

        // make sure that the customer is present
        User expectedUser = new User(
                name, username, email, password, avatar, role
        );

        assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedUser);

        // get customer by id

        int id = allUsers.stream()
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
    void canDeleteUser() {
        // Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String username = name;
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar";
        Role role = new Role(1, "user");

        UserRegistrationRequest request = new UserRegistrationRequest(
                name, username, email, password, avatar, 1
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
                name, username, email, password, avatar, role
        );

        assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedUser);

        // get customer by id

        int id = allUsers.stream()
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
    void canUpdateUserAllFields() {
        // Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String username = name;
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar";
        Role role = new Role(1, "user");

        UserRegistrationRequest request = new UserRegistrationRequest(
                name, username, email, password, avatar, 1
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
        Faker faker2 = new Faker();
        String newName = faker2.name().fullName();
        String newUsername = faker2.name().fullName();
        String newEmail = fakerName.lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String newPassword = "pa";
        String newAvatar = "av";
        Integer newRole = 2;

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                newName, newUsername, newEmail, newPassword, newAvatar, newRole
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
                id, newName, newUsername, newEmail, newPassword, newAvatar, new Role(2, "admin")
        );
        assertThat(updatedUser).isEqualTo(expected);
    }

}

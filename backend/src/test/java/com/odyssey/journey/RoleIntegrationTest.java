package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.odyssey.role.Role;
import com.odyssey.role.RoleRegistrationRequest;
import com.odyssey.role.RoleUpdateRequest;
import com.odyssey.user.User;
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
public class RoleIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String ROLE_URI = "/api/v1/roles";

    @Test
    void canRegisterARole() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();

        RoleRegistrationRequest request = new RoleRegistrationRequest(name);

        webTestClient.post()
                .uri(ROLE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RoleRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Role> allRoles = webTestClient.get()
                .uri(ROLE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Role>() {})
                .returnResult()
                .getResponseBody();

        Role expectedRole = new Role(
                name
        );

        assertThat(allRoles)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedRole);

        int id = allRoles.stream()
                .filter(role -> role.getName().equals(name))
                .map(Role::getId)
                .findFirst()
                .orElseThrow();

        expectedRole.setId(id);

        webTestClient.get()
                .uri(ROLE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Role>() {})
                .isEqualTo(expectedRole);
    }

    @Test
    void canDeleteRole() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();

        RoleRegistrationRequest request = new RoleRegistrationRequest(name);

        webTestClient.post()
                .uri(ROLE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RoleRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Role> allRoles = webTestClient.get()
                .uri(ROLE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Role>() {})
                .returnResult()
                .getResponseBody();

        Role expectedRole = new Role(
                name
        );

        assertThat(allRoles)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedRole);

        int id = allRoles.stream()
                .filter(role -> role.getName().equals(name))
                .map(Role::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(ROLE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(ROLE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateRoleAllFields() {
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();

        RoleRegistrationRequest request = new RoleRegistrationRequest(name);

        webTestClient.post()
                .uri(ROLE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RoleRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Role> allRoles = webTestClient.get()
                .uri(ROLE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Role>() {})
                .returnResult()
                .getResponseBody();

        int id = allRoles.stream()
                .filter(role -> role.getName().equals(name))
                .map(Role::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        Name name2 = faker2.name();
        String newName = name2.fullName();

        RoleUpdateRequest updateRequest = new RoleUpdateRequest(newName);

        webTestClient.put()
                .uri(ROLE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), RoleUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Role updatedRole = webTestClient.get()
                .uri(ROLE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Role.class)
                .returnResult()
                .getResponseBody();

        Role expected = new Role(
                id, newName
        );

        assertThat(updatedRole).isEqualTo(expected);
    }

}

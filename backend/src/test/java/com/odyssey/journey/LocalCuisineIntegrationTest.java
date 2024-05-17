package com.odyssey.journey;


import com.github.javafaker.Faker;
import com.odyssey.localCuisine.LocalCuisine;
import com.odyssey.locations.Location;
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
public class LocalCuisineIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String LOCALCUISINE_URI = "/api/v1/localCuisine";
    private static final String LOCATION_URI = "/api/v1/locations";

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
    void canRegisterAnLocalCuisine() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String desc = faker.name().fullName();
        String image = faker.name().fullName();
        Location location = setUpLocation();

        LocalCuisineRegistrationRequest request = new LocalCuisineRegistrationRequest(
                name, desc, image, location.getId()
        );
        webTestClient.post()
                .uri(LOCALCUISINE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LocalCuisineRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<LocalCuisine> allLocalCuisines = webTestClient.get()
                .uri(LOCALCUISINE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<LocalCuisine>() {})
                .returnResult()
                .getResponseBody();

        LocalCuisine expectedLocalCuisine = new LocalCuisine(
                name, desc, image, location
        );

        assertThat(allLocalCuisines)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedLocalCuisine);

        int id = allLocalCuisines.stream()
                .filter(localCuisine -> localCuisine.getName().equals(name) && localCuisine.getLocation().equals(location))
                .map(LocalCuisine::getId)
                .findFirst()
                .orElseThrow();

        expectedLocalCuisine.setId(id);

        webTestClient.get()
                .uri(LOCALCUISINE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<LocalCuisine>() {})
                .isEqualTo(expectedLocalCuisine);

    }

    @Test
    void canDeleteLocalCuisine() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String desc = faker.name().fullName();
        String image = faker.name().fullName();
        Location location = setUpLocation();

        LocalCuisineRegistrationRequest request = new LocalCuisineRegistrationRequest(
                name, desc, image, location.getId()
        );
        webTestClient.post()
                .uri(LOCALCUISINE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LocalCuisineRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<LocalCuisine> allLocalCuisines = webTestClient.get()
                .uri(LOCALCUISINE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<LocalCuisine>() {})
                .returnResult()
                .getResponseBody();

        LocalCuisine expectedLocalCuisine = new LocalCuisine(
                name, desc, image, location
        );

        assertThat(allLocalCuisines)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedLocalCuisine);

        int id = allLocalCuisines.stream()
                .filter(localCuisine -> localCuisine.getName().equals(name) && localCuisine.getLocation().equals(location))
                .map(LocalCuisine::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(LOCALCUISINE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(LOCALCUISINE_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateLocalCuisinesAllFields() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String desc = faker.name().fullName();
        String image = faker.name().fullName();
        Location location = setUpLocation();

        LocalCuisineRegistrationRequest request = new LocalCuisineRegistrationRequest(
                name, desc, image, location.getId()
        );
        webTestClient.post()
                .uri(LOCALCUISINE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), LocalCuisineRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<LocalCuisine> allLocalCuisiness = webTestClient.get()
                .uri(LOCALCUISINE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<LocalCuisine>() {})
                .returnResult()
                .getResponseBody();


        int id = allLocalCuisiness.stream()
                .filter(localCuisine -> localCuisine.getName().equals(name) && localCuisine.getLocation().equals(location))
                .map(LocalCuisine::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        String name2 = faker.name().fullName();
        String desc2 = faker.name().fullName();
        String image2 = faker.name().fullName();
        Location location2 = setUpLocation();
        LocalCuisineUpdateRequest updateRequest = new LocalCuisineUpdateRequest(
                name2, desc2, image2, location2.getId()
        );

        webTestClient.put()
                .uri(LOCALCUISINE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), LocalCuisineUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        LocalCuisine updatedLocalCuisine = webTestClient.get()
                .uri(LOCALCUISINE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(LocalCuisine.class)
                .returnResult()
                .getResponseBody();

        LocalCuisine expected = new LocalCuisine(
                id, name2, desc2, image2, location2
        );
        assertThat(updatedLocalCuisine).isEqualTo(expected);
    }
}

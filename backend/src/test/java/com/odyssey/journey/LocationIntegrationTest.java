package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationRequest;
import com.odyssey.locations.LocationUpdateRequest;
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
public class LocationIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String LOCATION_URI = "/api/v1/locations";

    @Test
    void canRegisterALocation() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = faker.name().fullName();
        String picture = faker.name().fullName();

        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city, country, picture
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

        Location expectedLocation = new Location(
                city, country, picture
        );

        assertThat(allLocations)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedLocation);

        int id = allLocations.stream()
                .filter(location -> location.getCity().equals(city) && location.getCountry().equals(country))
                .map(Location::getId)
                .findFirst()
                .orElseThrow();

        expectedLocation.setId(id);

        webTestClient.get()
                .uri(LOCATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Location>() {})
                .isEqualTo(expectedLocation);
    }

    @Test
    void canDeleteLocation() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = faker.name().fullName();
        String picture = faker.name().fullName();

        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city, country, picture
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

        Location expectedLocation = new Location(
                city, country, picture
        );

        assertThat(allLocations)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedLocation);

        int id = allLocations.stream()
                .filter(location -> location.getCity().equals(city) && location.getCountry().equals(country))
                .map(Location::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(LOCATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(LOCATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateLocationAllFields() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = faker.name().fullName();
        String picture = faker.name().fullName();

        LocationRegistrationRequest request = new LocationRegistrationRequest(
                city, country, picture
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

        String newCity = "Peja";
        String newCountry = "Kos";
        String newPicture = "Pic 2";

        LocationUpdateRequest updateRequest = new LocationUpdateRequest(
                newCity, newCountry, newPicture
        );

        webTestClient.put()
                .uri(LOCATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), LocationUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Location updatedLocation = webTestClient.get()
                .uri(LOCATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Location.class)
                .returnResult()
                .getResponseBody();

        Location expected = new Location(
                id, newCity, newCountry, newPicture
        );
        assertThat(updatedLocation).isEqualTo(expected);
    }
}

package com.odyssey.journey;

import com.github.javafaker.Faker;
import com.odyssey.hotels.Hotel;
import com.odyssey.hotels.HotelRegistrationRequest;
import com.odyssey.hotels.HotelUpdateRequest;
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
public class HotelIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String HOTEL_URI = "/api/v1/hotels";
    private static final String LOCATION_URI = "/api/v1/locations";

    private Location setUpLocation() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = city;
        String picture = "pic";
//        LocationRegistrationRequest request = new LocationRegistrationRequest(
//                city,
//                country,
//                picture
//        );
//        webTestClient.post()
//                .uri(LOCATION_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), LocationRegistrationRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
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
    void canRegisterAHotel() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        Location location = setUpLocation();
        Double rating = (double) Math.round(RANDOM.nextDouble(1.0, 3.0));
        String link = name;
        HotelRegistrationRequest request = new HotelRegistrationRequest(
            name, location.getId(), rating, link
        );

        webTestClient.post()
                .uri(HOTEL_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), HotelRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Hotel> allHotels = webTestClient.get()
                .uri(HOTEL_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Hotel>() {})
                .returnResult()
                .getResponseBody();

        Hotel expectedHotel = new Hotel(
                name, location, rating, link
        );

        assertThat(allHotels)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedHotel);

        int id = allHotels.stream()
                .filter(hotel -> hotel.getName().equals(name) && hotel.getLocation().equals(location))
                .map(Hotel::getId)
                .findFirst()
                .orElseThrow();

        expectedHotel.setId(id);

        webTestClient.get()
                .uri(HOTEL_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Hotel>() {})
                .isEqualTo(expectedHotel);

    }

    @Test
    void canDeleteHotel() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        Location location = setUpLocation();
        Double rating = (double) Math.round(RANDOM.nextDouble(1.0, 3.0));
        String link = name;

        HotelRegistrationRequest request = new HotelRegistrationRequest(
                name, location.getId(), rating, link
        );

        webTestClient.post()
                .uri(HOTEL_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), HotelRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Hotel> allHotels = webTestClient.get()
                .uri(HOTEL_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Hotel>() {})
                .returnResult()
                .getResponseBody();

        Hotel expectedHotel = new Hotel(
                name, location, rating, link
        );

        assertThat(allHotels)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedHotel);

        int id = allHotels.stream()
                .filter(hotel -> hotel.getName().equals(name) && hotel.getLocation().equals(location))
                .map(Hotel::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(HOTEL_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(HOTEL_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateHotelAllFields() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        Location location = setUpLocation();
        Double rating = 3.1;
        String link = name;

        HotelRegistrationRequest request = new HotelRegistrationRequest(
                name, location.getId(), rating, link
        );

        webTestClient.post()
                .uri(HOTEL_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), HotelRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Hotel> allHotels = webTestClient.get()
                .uri(HOTEL_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Hotel>() {})
                .returnResult()
                .getResponseBody();

        int id = allHotels.stream()
                .filter(hotel -> hotel.getName().equals(name) && hotel.getLocation().equals(location))
                .map(Hotel::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        String name2 = faker2.name().fullName();
        Location location2 = setUpLocation();
        Double rating2 = 3.3;
        String link2 = name;
        HotelUpdateRequest updateRequest = new HotelUpdateRequest (
                name2, location2.getId(), rating2, link2
        );

        webTestClient.put()
                .uri(HOTEL_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), HotelUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Hotel updatedHotel = webTestClient.get()
                .uri(HOTEL_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Hotel.class)
                .returnResult()
                .getResponseBody();

        Hotel expected = new Hotel (
                id, name2, location2, rating2, link2
        );
        assertThat(updatedHotel).isEqualTo(expected);

    }



}

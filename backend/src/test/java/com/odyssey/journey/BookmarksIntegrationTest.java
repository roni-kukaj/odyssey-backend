package com.odyssey.journey;


import com.github.javafaker.Faker;
import com.odyssey.bookmarks.Bookmarks;
import com.odyssey.bookmarks.BookmarksRegistrationRequest;
import com.odyssey.bookmarks.BookmarksUpdateRequest;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationRequest;
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

public class BookmarksIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String BOOKMARKS_URI = "/api/v1/bookmarks";
    private static final String LOCATION_URI = "/api/v1/locations";
    private static final String USER_URI = "/api/v1/users";


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

    private User setUpUser(){
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String username = name;
        String email = faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "1234";
        String avatar = "avatar";
        Role role = new Role();
        int roleid = 2;
        role.setId(roleid);

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                name,username,email,password,avatar,roleid
        );
        webTestClient.post()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        List<User> allAuthors = webTestClient.get()
                .uri(USER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<User>() {})
                .returnResult()
                .getResponseBody();

        int id = allAuthors.stream()
                .filter(user -> user.getFullname().equals(name) && user.getUsername().equals(username))
                .map(User::getId)
                .findFirst()
                .orElseThrow();
        return new User(id,name,username,email,password,avatar,role);
    }

    @Test
    void canAddBookmarks() {
        Faker faker = new Faker();
        User user = setUpUser();
        Location location = setUpLocation();

        BookmarksRegistrationRequest bookmarksRegistrationRequest = new BookmarksRegistrationRequest(
               location.getId(),user.getId()
        );

        webTestClient.post()
                .uri(BOOKMARKS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookmarksRegistrationRequest), BookmarksRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Bookmarks> allBookmarks = webTestClient.get()
                .uri(BOOKMARKS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Bookmarks>() {})
                .returnResult()
                .getResponseBody();

        Bookmarks expectedBookmarks = new Bookmarks(
               location,user
        );

        assertThat(allBookmarks)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedBookmarks);

        int id = allBookmarks.stream()
                .filter(bookmarks -> bookmarks.getLocation().equals(location) && bookmarks.getUser().equals(user))
                .map(Bookmarks::getId)
                .findFirst()
                .orElseThrow();

        expectedBookmarks.setId(id);

        webTestClient.get()
                .uri(BOOKMARKS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Bookmarks>() {})
                .isEqualTo(expectedBookmarks);
    }

    @Test
    void canDeleteBookmarks() {
        Faker faker = new Faker();
        Location location = setUpLocation();
        User user = setUpUser();

        BookmarksRegistrationRequest bookmarksRegistrationRequest = new BookmarksRegistrationRequest(
               location.getId(),user.getId()
        );


        webTestClient.post()
                .uri(BOOKMARKS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookmarksRegistrationRequest), BookmarksRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Bookmarks> allBookmarks = webTestClient.get()
                .uri(BOOKMARKS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Bookmarks>() {})
                .returnResult()
                .getResponseBody();

        Bookmarks expectedBookmarks = new Bookmarks(
                location,user
        );

        assertThat(allBookmarks)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedBookmarks);

        int id = allBookmarks.stream()
                .filter(bookmarks -> bookmarks.getLocation().equals(location) && bookmarks.getUser().equals(user))
                .map(Bookmarks::getId)
                .findFirst()
                .orElseThrow();



        webTestClient.delete()
                .uri(BOOKMARKS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(BOOKMARKS_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateBookmarks() {
        Faker faker = new Faker();
        Location location = setUpLocation();
        User user = setUpUser();

        BookmarksRegistrationRequest bookmarksRegistrationRequest = new BookmarksRegistrationRequest(
                location.getId(), user.getId()
        );


        webTestClient.post()
                .uri(BOOKMARKS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookmarksRegistrationRequest), BookmarksRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Bookmarks> allBookmarks = webTestClient.get()
                .uri(BOOKMARKS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Bookmarks>() {})
                .returnResult()
                .getResponseBody();

        int id = allBookmarks.stream()
                .filter(bookmarks -> bookmarks.getLocation().equals(location) && bookmarks.getUser().equals(user))
                .map(Bookmarks::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        Location location1 = setUpLocation();
        User user1 = setUpUser();
        BookmarksUpdateRequest bookmarksUpdateRequest = new BookmarksUpdateRequest (
                location1.getId(),user1.getId()
        );

        webTestClient.put()
                .uri(BOOKMARKS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookmarksUpdateRequest), BookmarksUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Bookmarks updatedBookmarks = webTestClient.get()
                .uri(BOOKMARKS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Bookmarks.class)
                .returnResult()
                .getResponseBody();

        Bookmarks expected = new Bookmarks (
                location1,user1
        );
        assertThat(updatedBookmarks).isEqualTo(expected);
    }
}

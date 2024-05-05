package com.odyssey.journey;


import com.github.javafaker.Faker;
import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityRegistrationRequest;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationRequest;
import com.odyssey.recommendations.Recommendation;
import com.odyssey.recommendations.RecommendationRegistrationRequest;
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
public class RecommendationIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String RECOMMENDATION_URI  = "/api/v1/recommendations";
    private static final String USER_URI = "/api/v1/users";
    private static final String ACTIVITY_URI = "/api/v1/activities";
    private static final String LOCATION_URI = "/api/v1/locations";


    private User setUpUser(){
        Faker faker = new Faker();

        String name = faker.name().fullName();
        String username = name;
        String email = faker.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";
        String password = "123";
        String avatar = "avatar";
        Role role = new Role();
        int roleid = 1;
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
        return new User(id, name,username,email,password,avatar,role);
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

    private Activity setUpActivity(){
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String description = "Swimming in the Mediterranean Sea";
        Integer cost = 5;
        Integer duration = 1;

       Location location =  setUpLocation();


        ActivityRegistrationRequest activityRegistrationRequest = new ActivityRegistrationRequest(
               name,description,cost,duration,location.getId()
        );

        webTestClient.post()
                .uri(ACTIVITY_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(activityRegistrationRequest), ActivityRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        List<Activity> allActivities = webTestClient.get()
                .uri(ACTIVITY_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Activity>() {})
                .returnResult()
                .getResponseBody();

        int id = allActivities.stream()
                .filter(activity -> activity.getName().equals(name) && activity.getDescription().equals(description)&&activity.getLocation().equals(location)
                      )
                .map(Activity::getId)
                .findFirst()
                .orElseThrow();
        return new Activity(id, name,description,cost,duration,location);

    }

    @Test
    void canRegisterARecommendation() {
        Faker faker = new Faker();
        String description = faker.name().fullName();

        User user = setUpUser();
        Activity activity = setUpActivity();

        RecommendationRegistrationRequest recommendationRegistrationRequest = new RecommendationRegistrationRequest(
                description,user.getId(),activity.getId()
        );
        webTestClient.post()
                .uri(RECOMMENDATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(recommendationRegistrationRequest), RecommendationRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Recommendation> allRecommendations = webTestClient.get()
                .uri(RECOMMENDATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Recommendation>() {})
                .returnResult()
                .getResponseBody();

        Recommendation expectedRecommendation = new Recommendation(
               description,user,activity
        );

        assertThat(allRecommendations)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedRecommendation);

        int id = allRecommendations.stream()
                .filter(recommendation -> recommendation.getDescription().equals(description)&&recommendation.getUser().equals(user)&&recommendation.getActivity().equals(activity)
                )
                .map(Recommendation::getId)
                .findFirst()
                .orElseThrow();

        expectedRecommendation.setId(id);

        webTestClient.get()
                .uri(RECOMMENDATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Recommendation>() {})
                .isEqualTo(expectedRecommendation);
    }

    @Test
    void canDeleteRecommendation() {
        Faker faker = new Faker();

        String desc = faker.name().fullName();
        User user  = setUpUser();
        Activity activity = setUpActivity();

        RecommendationRegistrationRequest request = new RecommendationRegistrationRequest(
               desc, user.getId(),activity.getId()
        );
        webTestClient.post()
                .uri(RECOMMENDATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RecommendationRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Recommendation> allRecommendations = webTestClient.get()
                .uri(RECOMMENDATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Recommendation>() {})
                .returnResult()
                .getResponseBody();

        Recommendation expectedRecommendation = new Recommendation(
                 desc,user,activity
        );

        assertThat(allRecommendations)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedRecommendation);

        int id = allRecommendations.stream()
                .filter(recommendation -> recommendation.getDescription().equals(desc) && recommendation.getUser().equals(user) &&recommendation.getActivity().equals(activity))
                .map(Recommendation::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(RECOMMENDATION_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(RECOMMENDATION_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();

    }
}

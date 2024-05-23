package com.odyssey.journey;
import com.github.javafaker.Faker;
import com.odyssey.locations.Location;
import com.odyssey.plans.Plan;
import com.odyssey.plans.PlanRegistrationRequest;
import com.odyssey.plans.PlanUpdateRequest;
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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    private static final String PLAN_URI = "/api/v1/plans";
    private static final String LOCATION_URI = "/api/v1/locations";
    private static final String USER_URI = "/api/v1/users";

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
    private User setUpUser() {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String username = name;
        String email = name + "@gmail.com";
        String password = "passi";
        String avatar = "avatar";
        Role role = new Role(1, "user");
        UserRegistrationRequest request = new UserRegistrationRequest(
                name, username, email, password, avatar, role.getId()
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

    @Test
    void canRegisterAPlan() throws ParseException {
        User user = setUpUser();
        Location location = setUpLocation();
        LocalDate date = LocalDate.now();

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                user.getId(), location.getId(), date
        );

        webTestClient.post()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PlanRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Plan> allPlans = webTestClient.get()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Plan>() {})
                .returnResult()
                .getResponseBody();

        Plan expectedPlan = new Plan(
                user, location, date
        );

        assertThat(allPlans)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPlan);

        int id = allPlans.stream()
                .filter(plan -> plan.getUser().equals(user) && plan.getLocation().equals(location))
                .map(Plan::getId)
                .findFirst()
                .orElseThrow();

        expectedPlan.setId(id);

        webTestClient.get()
                .uri(PLAN_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Plan>() {})
                .isEqualTo(expectedPlan);
    }

    @Test
    void canDeletePlanById() throws ParseException {
        User user = setUpUser();
        Location location = setUpLocation();
        LocalDate date = LocalDate.now();

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                user.getId(), location.getId(), date
        );

        webTestClient.post()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PlanRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Plan> allPlans = webTestClient.get()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Plan>() {})
                .returnResult()
                .getResponseBody();

        Plan expectedPlan = new Plan(
                user, location, date
        );

        assertThat(allPlans)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPlan);

        int id = allPlans.stream()
                .filter(plan -> plan.getUser().equals(user) && plan.getLocation().equals(location))
                .map(Plan::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(PLAN_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(PLAN_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canDeletePlanByUserId() throws ParseException {
        User user = setUpUser();
        Location location = setUpLocation();
        LocalDate date = LocalDate.now();

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                user.getId(), location.getId(), date
        );

        webTestClient.post()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PlanRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Plan> allPlans = webTestClient.get()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Plan>() {})
                .returnResult()
                .getResponseBody();

        Plan expectedPlan = new Plan(
                user, location, date
        );

        assertThat(allPlans)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedPlan);

        int id = allPlans.stream()
                .filter(plan -> plan.getUser().equals(user) && plan.getLocation().equals(location))
                .map(Plan::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(PLAN_URI + "/user/{id}", user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(PLAN_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdatePlanAllFields() throws ParseException {
        User user = setUpUser();
        Location location = setUpLocation();
        LocalDate date = LocalDate.now();

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                user.getId(), location.getId(), date
        );

        webTestClient.post()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), PlanRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<Plan> allPlans = webTestClient.get()
                .uri(PLAN_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Plan>() {})
                .returnResult()
                .getResponseBody();

        int id = allPlans.stream()
                .filter(plan -> plan.getUser().equals(user) && plan.getLocation().equals(location))
                .map(Plan::getId)
                .findFirst()
                .orElseThrow();

        Location location2 = setUpLocation();
//        LocalDate date2 = date.plus(90, ChronoUnit.DAYS);
//        PlanUpdateRequest updateRequest = new PlanUpdateRequest(
//                user.getId(), location2.getId(), date2
//        );
//
//        webTestClient.put()
//                .uri(PLAN_URI + "/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(updateRequest), PlanUpdateRequest.class)
//                .exchange()
//                .expectStatus()
//                .isOk();

        Plan updatedPlan = webTestClient.get()
                .uri(PLAN_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Plan.class)
                .returnResult()
                .getResponseBody();

//        Plan expected = new Plan(
//                id, user, location2, date2
//        );
//        assertThat(updatedPlan).isEqualTo(expected);
    }

}

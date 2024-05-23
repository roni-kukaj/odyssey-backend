package com.odyssey.journey;
import com.github.javafaker.Faker;
import com.odyssey.activities.Activity;
import com.odyssey.activities.ActivityRegistrationRequest;
import com.odyssey.activities.ActivityUpdateRequest;
import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();

    @Autowired
    private CloudinaryService cloudinaryService;

    private final String FILE_URL = "src/main/resources/images/test.png";

    private static final String ACTIVITY_URI = "/api/v1/activities";
    private static final String LOCATION_URI = "/api/v1/locations";

    private Location setUpLocation() throws IOException {
//        Faker faker = new Faker();
//        String city = faker.name().fullName();
//        String country = city;
//
//        Path path = Paths.get(FILE_URL);
//        byte[] content = Files.readAllBytes(path);
//
//
//        MockMultipartFile image = new MockMultipartFile(
//                "file",
//                "test.png",
//                "image/png",
//                content
//        );
//
//        LocationRegistrationDto dto = new LocationRegistrationDto(
//                city,
//                country,
//                image
//        );
//
//        webTestClient.post()
//                .uri(LOCATION_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(dto), LocationRegistrationDto.class)
//                .exchange()
//                .expectStatus()
//                .isOk();
//        List<Location> allLocations = webTestClient.get()
//                .uri(LOCATION_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Location>() {})
//                .returnResult()
//                .getResponseBody();
//
//        int id = allLocations.stream()
//                .filter(location -> location.getCity().equals(city) && location.getCountry().equals(country))
//                .map(Location::getId)
//                .findFirst()
//                .orElseThrow();
//
//        String url = allLocations.stream()
//                .filter(location -> location.getId().equals(id))
//                .map(Location::getPicture)
//                .findFirst()
//                .orElseThrow();
//
//        return new Location(id, city, country, url);
        return new Location();
    }

    @Test
    void canRegisterAnActivity() throws IOException {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String desc = faker.name().fullName();
        Integer cost = RANDOM.nextInt(1, 100);
        Integer duration = RANDOM.nextInt(1, 100);
        Location location = setUpLocation();

        ActivityRegistrationRequest request = new ActivityRegistrationRequest(
                name, desc, cost, duration, location.getId()
        );

        webTestClient.post()
                .uri(ACTIVITY_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ActivityRegistrationRequest.class)
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

        Activity expectedActivity = new Activity(
                name, desc, cost, duration, location
        );

        assertThat(allActivities)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedActivity);

        int id = allActivities.stream()
                .filter(activity -> activity.getName().equals(name) && activity.getLocation().equals(location))
                .map(Activity::getId)
                .findFirst()
                .orElseThrow();

        expectedActivity.setId(id);

        webTestClient.get()
                .uri(ACTIVITY_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Activity>() {})
                .isEqualTo(expectedActivity);

        cloudinaryService.deleteImageByUrl(location.getPicture());

    }

    @Test
    void canDeleteActivity() throws IOException {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String desc = faker.name().fullName();
        Integer cost = RANDOM.nextInt(1, 100);
        Integer duration = RANDOM.nextInt(1, 100);
        Location location = setUpLocation();

        ActivityRegistrationRequest request = new ActivityRegistrationRequest(
                name, desc, cost, duration, location.getId()
        );
        webTestClient.post()
                .uri(ACTIVITY_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ActivityRegistrationRequest.class)
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

        Activity expectedActivity = new Activity(
                name, desc, cost, duration, location
        );

        assertThat(allActivities)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedActivity);

        int id = allActivities.stream()
                .filter(activity -> activity.getName().equals(name) && activity.getLocation().equals(location))
                .map(Activity::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(ACTIVITY_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(ACTIVITY_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateActivityAllFields() throws IOException {
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String desc = faker.name().fullName();
        Integer cost = RANDOM.nextInt(1, 100);
        Integer duration = RANDOM.nextInt(1, 100);
        Location location = setUpLocation();

        ActivityRegistrationRequest request = new ActivityRegistrationRequest(
                name, desc, cost, duration, location.getId()
        );
        webTestClient.post()
                .uri(ACTIVITY_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), ActivityRegistrationRequest.class)
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
                .filter(activity -> activity.getName().equals(name) && activity.getLocation().equals(location))
                .map(Activity::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        String name2 = faker.name().fullName();
        String desc2 = faker.name().fullName();
        Integer cost2 = RANDOM.nextInt(1, 100);
        Integer duration2 = RANDOM.nextInt(1, 100);
        Location location2 = setUpLocation();
        ActivityUpdateRequest updateRequest = new ActivityUpdateRequest(
                name2, desc2, cost2, duration2, location2.getId()
        );

        webTestClient.put()
                .uri(ACTIVITY_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), ActivityUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Activity updatedActivity = webTestClient.get()
                .uri(ACTIVITY_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Activity.class)
                .returnResult()
                .getResponseBody();

        Activity expected = new Activity(
                id, name2, desc2, cost2, duration2, location2
        );
        assertThat(updatedActivity).isEqualTo(expected);
    }
}

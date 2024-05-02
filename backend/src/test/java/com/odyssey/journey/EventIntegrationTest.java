package com.odyssey.journey;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.odyssey.events.Event;
import com.odyssey.events.EventRegistrationRequest;
import com.odyssey.events.EventUpdateRequest;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationRegistrationRequest;
import com.odyssey.locations.LocationUpdateRequest;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class EventIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String EVENT_URI = "/api/v1/events";
    private static final String LOCATION_URI = "/api/v1/locations";


    private Location setUpLocation() {
        Faker faker = new Faker();
        String city = faker.name().fullName();
        String country = city;
        String picture = "picture 1";
        LocationRegistrationRequest locationRegistrationRequest = new LocationRegistrationRequest(
                city,
                country,
                picture
        );

        webTestClient.post().uri(LOCATION_URI).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).body(Mono.just(locationRegistrationRequest),LocationRegistrationRequest.class)
                .exchange().expectStatus().isOk();

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
    void canRegisterAnEvent() {
        Faker faker = new Faker();

        String name = faker.name().fullName();
        String description = faker.name().fullName();
        String image = faker.name().fullName();
        LocalDate date = LocalDate.of(2024,11,28);
        Double cost = 75.0;
        Integer duration = 1;
        Location location = setUpLocation();


        EventRegistrationRequest eventRegistrationRequest =
                new EventRegistrationRequest(name,description,image,date,cost,duration,location.getId());

        webTestClient.post().uri(EVENT_URI)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(eventRegistrationRequest),EventRegistrationRequest.class).exchange()
                .expectStatus().isOk();

        List<Event> allEvents = webTestClient.get().uri(EVENT_URI)
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk().expectBodyList(new ParameterizedTypeReference<Event>() {
                }).returnResult().getResponseBody();
        Event expectedEvent = new Event(name,description,image,date,cost,duration,location);

        assertThat(allEvents).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedEvent);

        int id =  allEvents.stream().filter(event -> event.getName().equals(name)&&event.getLocation_id().equals(location)).map(Event::getId).findFirst().orElseThrow();
        expectedEvent.setId(id);

        webTestClient.get().uri(EVENT_URI + "/{id}" , id).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().expectBody(new ParameterizedTypeReference<Event>() {
                }).isEqualTo(expectedEvent);
    }


    @Test
    void canDeleteEvent() {
        Faker faker = new Faker();

        String name = faker.name().fullName();
        String description = faker.name().fullName();
        String image = faker.name().fullName();
        LocalDate date = LocalDate.of(2024,11,28);
        Double cost = 75.0;
        Integer duration = 1;

        Location location = setUpLocation();



        EventRegistrationRequest eventRegistrationRequest = new EventRegistrationRequest(name,description,image,date,cost,duration,location.getId());

        webTestClient.post().uri(EVENT_URI)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(eventRegistrationRequest),EventRegistrationRequest.class).exchange()
                .expectStatus().isOk();

        List<Event> allEvents = webTestClient.get().uri(EVENT_URI)
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk().expectBodyList(new ParameterizedTypeReference<Event>() {
                }).returnResult().getResponseBody();
        Event expectedEvent = new Event(name,description,image,date,cost,duration,location);
        assertThat(allEvents).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedEvent);

        int id =  allEvents.stream().filter(event -> event.getName().equals(name)).map(Event::getId).findFirst().orElseThrow();
        expectedEvent.setId(id);

        webTestClient.delete().uri(EVENT_URI + "/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(EVENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();


    }


    @Test
    void canUpdateEventAllFields() {
        Faker faker = new Faker();

        String name = faker.name().fullName();
        String description = faker.name().fullName();
        String image = faker.name().fullName();
        LocalDate date = LocalDate.of(2024,11,28);
        Double cost = 75.0;
        Integer duration = 1;

        Location location = setUpLocation();



        EventRegistrationRequest eventRegistrationRequest =
                new EventRegistrationRequest(name,description,image,date,cost,duration,location.getId());
        webTestClient.post().uri(EVENT_URI)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(eventRegistrationRequest),EventRegistrationRequest.class).exchange()
                .expectStatus().isOk();

        List<Event> allEvents = webTestClient.get().uri(EVENT_URI)
                .accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk().expectBodyList(new ParameterizedTypeReference<Event>() {
                }).returnResult().getResponseBody();

        int id =
                allEvents.stream().filter(event -> event.getName().equals(name)).map(Event::getId).findFirst().orElseThrow();

        String newname = "Wimbledon Championships";
        String newdescription = "Tennis tournament";
        String newimage = "image 2";
        LocalDate newdate = LocalDate.of(2024,06,25);
        Double newcost = 150.5;
        Integer newduration = 5;
        Location newlocation = setUpLocation();

        EventUpdateRequest eventUpdateRequest = new EventUpdateRequest(newname,newdescription,newimage,newdate,newcost,newduration,newlocation.getId());
        webTestClient.put()
                .uri(EVENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(eventUpdateRequest), EventUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Event updatedEvent = webTestClient.get()
                .uri(EVENT_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Event.class)
                .returnResult()
                .getResponseBody();

        Event expected = new Event(
                id, newname,newdescription,newimage,newdate,newcost,newduration,newlocation
        );
        assertThat(updatedEvent).isEqualTo(expected);




    }

   
}



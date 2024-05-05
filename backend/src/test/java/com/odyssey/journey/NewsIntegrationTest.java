package com.odyssey.journey;


import com.github.javafaker.Faker;
import com.odyssey.news.News;
import com.odyssey.news.NewsRegistrationRequest;
import com.odyssey.news.NewsUpdateRequest;
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
public class NewsIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private static final String NEWS_URI = "/api/v1/news";
    private static final String AUTHOR_URI = "/api/v1/users";

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
        role.setName("admin");

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                name,username,email,password,avatar,roleid
        );
        webTestClient.post()
                .uri(AUTHOR_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(userRegistrationRequest), UserRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        List<User> allAuthors = webTestClient.get()
                .uri(AUTHOR_URI)
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
    void canAddNews() {
        Faker faker = new Faker();
        String title = faker.name().fullName();
        String description = faker.name().fullName();
        String picture = "picture";
        User author = setUpUser();

        NewsRegistrationRequest newsRegistrationRequest = new NewsRegistrationRequest(
               author.getId(),title,description,picture
        );

        webTestClient.post()
                .uri(NEWS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newsRegistrationRequest), NewsRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<News> allNews = webTestClient.get()
                .uri(NEWS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<News>() {})
                .returnResult()
                .getResponseBody();

        News expectedNews = new News(
                author,title,description,picture
        );

        assertThat(allNews)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedNews);

        int id = allNews.stream()
                .filter(news -> news.getTitle().equals(title) && news.getAuthor().equals(author))
                .map(News::getId)
                .findFirst()
                .orElseThrow();

        expectedNews.setId(id);

        webTestClient.get()
                .uri(NEWS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<News>() {})
                .isEqualTo(expectedNews);
    }

    @Test
    void canDeleteNews() {
        Faker faker = new Faker();
        String title = faker.name().fullName();
        String description = faker.name().fullName();
        String picture = "picture";
        User author = setUpUser();

        NewsRegistrationRequest newsRegistrationRequest = new NewsRegistrationRequest(
                author.getId(),title,description,picture
        );


        webTestClient.post()
                .uri(NEWS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newsRegistrationRequest), NewsRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<News> allNews = webTestClient.get()
                .uri(NEWS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<News>() {})
                .returnResult()
                .getResponseBody();

        News expectedNews = new News(
                author,title,description,picture
        );

        assertThat(allNews)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedNews);

        int id = allNews.stream()
                .filter(news -> news.getTitle().equals(title) && news.getAuthor().equals(author))
                .map(News::getId)
                .findFirst()
                .orElseThrow();



        webTestClient.delete()
                .uri(NEWS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(NEWS_URI + "/{id}", id)
                .accept()
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateNews() {
        Faker faker = new Faker();
        String title = faker.name().fullName();
        String description = faker.name().fullName();
        String picture = "picture";
        User author = setUpUser();

        NewsRegistrationRequest newsRegistrationRequest = new NewsRegistrationRequest(
                author.getId(),title,description,picture
        );


        webTestClient.post()
                .uri(NEWS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newsRegistrationRequest), NewsRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        List<News> allNews = webTestClient.get()
                .uri(NEWS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<News>() {})
                .returnResult()
                .getResponseBody();

        int id = allNews.stream()
                .filter(news -> news.getTitle().equals(title) && news.getAuthor().equals(author))
                .map(News::getId)
                .findFirst()
                .orElseThrow();

        Faker faker2 = new Faker();
        String title2 = faker.name().fullName();
        String description2 = faker.name().fullName();
        String picture2 = "picture 2";
        User author2 = setUpUser();
        NewsUpdateRequest newsUpdateRequest = new NewsUpdateRequest (
                author2.getId(),title2,description2,picture2
        );

        webTestClient.put()
                .uri(NEWS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newsUpdateRequest), NewsUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        News updatedNews = webTestClient.get()
                .uri(NEWS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(News.class)
                .returnResult()
                .getResponseBody();

        News expected = new News (
                id,author2,title2,description2,picture2
        );
        assertThat(updatedNews).isEqualTo(expected);

    }
    }


package com.odyssey.news;

import com.odyssey.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewsTest {

    @Test
    void testNewsGettersAndSetters() {
        News news = new News();
        User author = new User();
        news.setId(1);
        news.setDescription("News");
        news.setPicture("News picture");
        news.setTitle("Title");
        news.setAuthor(author);

        assertEquals(1,news.getId());
        assertEquals("News",news.getDescription());
        assertEquals("Title",news.getTitle());
        assertEquals("News picture",news.getPicture());
        assertEquals(author,news.getAuthor());
    }
}

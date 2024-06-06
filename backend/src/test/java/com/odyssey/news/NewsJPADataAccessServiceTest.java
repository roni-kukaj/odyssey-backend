package com.odyssey.news;

import com.odyssey.models.News;
import com.odyssey.models.User;
import com.odyssey.repositories.NewsRepository;
import com.odyssey.services.data.NewsJPADataAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class NewsJPADataAccessServiceTest {

    private NewsJPADataAccessService newsJPADataAccessService;
    private AutoCloseable autoCloseable;


    @Mock
    private NewsRepository newsRepository;

    @BeforeEach
    void setUp(){
    autoCloseable = MockitoAnnotations.openMocks(this);
    newsJPADataAccessService = new NewsJPADataAccessService(newsRepository);

    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllNews() {
        newsJPADataAccessService.selectAllNews();
        verify(newsRepository).findAll();
    }

    @Test
    void selectNewsById() {
        int id=1;
        newsJPADataAccessService.selectNewsById(id);
        verify(newsRepository).findById(id);
    }

    @Test
    void selectNewsByAuthorId() {
        int author_id = 1;
        newsJPADataAccessService.selectNewsByAuthorId(author_id);
        verify(newsRepository).findNewsByAuthorId(author_id);
    }

    @Test
    void insertNews() {

        News news = new News("Title","Description","Picture", new User());
        newsJPADataAccessService.insertNews(news);
        verify(newsRepository).save(news);
    }

    @Test
    void existsNewsById() {
        int id = 1;
        newsJPADataAccessService.existsNewsById(id);
        verify(newsRepository).existsNewsById(id);
    }


    @Test
    void existsNewsByTitleAndAuthorId() {
        int author_id = 1;
        String title = "Title";
        newsJPADataAccessService.existsNewsByTitleAndAuthorId(title,author_id);
        verify(newsRepository).existsNewsByTitleAndAuthorId(title,author_id);
    }

    @Test
    void updateNews() {
        News news = new News("title","description","picture", new User());
        newsJPADataAccessService.updateNews(news);
        verify(newsRepository).save(news);
    }

    @Test
    void deleteNews() {
        int id = 4;
        newsJPADataAccessService.deleteNewsById(id);
        verify(newsRepository).deleteById(id);
    }
}

package com.odyssey.news;

import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)


public class NewsServiceTest {

    @Mock
    private NewsDao newsDao;
    @Mock
    private UserDao authorDao;

    private NewsService underTest;

    @BeforeEach
    void setUp(){
        underTest = new NewsService(newsDao,authorDao);
    }

    @Test
    void getAllNews() {
        underTest.getAllNews();
        verify(newsDao).selectAllNews();
    }

    @Test
    void getNews() {
        int id = 1;
        News news = new News(id,new User(),"title","description","picture");
        when(newsDao.selectNewsById(id)).thenReturn(Optional.of(news));
        News news1 = underTest.getNews(id);
        assertThat(news1).isEqualTo(news);
    }


    @Test
    void getNewsByAuthorId() {
        User author = new User();
        int authorId = 2;
        author.setId(authorId);

        when(authorDao.existsUserById(authorId)).thenReturn(false);
        assertThatThrownBy(()-> underTest.getNewsByAuthorId(authorId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("author with id [%s] not found".formatted(authorId));

        verify(newsDao,never()).insertNews(any());
    }

    @Test
    void willThrowWhenGetNewsReturnsEmptyOptional() {
        int id = 3;
        when(newsDao.selectNewsById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()->underTest.getNews(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("news with id [%s] not found".formatted(id));
    }

    @Test
    void addNews() {
        User author = new User();
        int authorId = 3;
        author.setId(authorId);

        String title = "News title";
        String description = "News description";
        String picture = "News picture";

        NewsRegistrationRequest newsRegistrationRequest = new NewsRegistrationRequest(
                authorId,title,description,picture
        );

        when(authorDao.selectUserById(authorId)).thenReturn(Optional.of(author));
        when(newsDao.existsNewsByTitleAndAuthorId(title,authorId)).thenReturn(false);

        underTest.addNews(newsRegistrationRequest);

        ArgumentCaptor<News> newsArgumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsDao).insertNews(newsArgumentCaptor.capture());

        News capturedNews = newsArgumentCaptor.getValue();

        Assertions.assertThat(capturedNews.getId()).isNull();
        Assertions.assertThat(capturedNews.getAuthor().getId()).isEqualTo(newsRegistrationRequest.authorId());
        Assertions.assertThat(capturedNews.getTitle()).isEqualTo(newsRegistrationRequest.title());
        Assertions.assertThat(capturedNews.getDescription()).isEqualTo(newsRegistrationRequest.description());
        Assertions.assertThat(capturedNews.getPicture()).isEqualTo(newsRegistrationRequest.picture());


    }


    @Test
    void willThrowWhenAuthorNotExists() {
        User author = new User();
        int authorId = 1;
        author.setId(authorId);
        String title = "News title";
        String description = "Description";
        String picture = "Picture 2";
        NewsRegistrationRequest newsRegistrationRequest = new NewsRegistrationRequest(
                authorId,title,description,picture
        );

        when(authorDao.selectUserById(authorId)).thenReturn(Optional.empty());
        lenient().when(newsDao.existsNewsByTitleAndAuthorId(title,authorId)).thenReturn(false);
        assertThatThrownBy(()->underTest.addNews(newsRegistrationRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("author with id [%s] not found".formatted(authorId));

        verify(newsDao,never()).insertNews(any());
    }


    @Test
    void deleteNews() {
        int id = 5;
        when(newsDao.existsNewsById(id)).thenReturn(true);
        underTest.deleteNews(id);
        verify(newsDao).deleteNewsById(id);
    }


    @Test
    void willThrowDeleteNewsNotExists() {
        int id = 5;
        lenient().when(newsDao.existsNewsById(id)).thenReturn(false);

        Assertions.assertThatThrownBy(()-> underTest.deleteNews(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("news with id [%s] not found".formatted(id));

        verify(newsDao,never()).deleteNewsById(any());
    }

    @Test
    void updateNews() {
        int id = 3;
        User author = new User();
        author.setId(1);
        String title = "Title";
        String description = "The best";
        String picture = "Picture 1";

        News news = new News(author,title,description,picture);
        when(newsDao.selectNewsById(id)).thenReturn(Optional.of(news));

        String newtitle = "New title";
        String newdescription = "New description";
        String newpicture = "New picture";
        User author1 = new User();
        author1.setId(2);

        NewsUpdateRequest newsUpdateRequest = new NewsUpdateRequest(author1.getId(), newtitle,newdescription, newpicture);
        when(authorDao.selectUserById(author1.getId())).thenReturn(Optional.of(author1));

        underTest.updateNews(id,newsUpdateRequest);

        ArgumentCaptor<News> newsArgumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsDao).updateNews(newsArgumentCaptor.capture());

        News capturedNews = newsArgumentCaptor.getValue();

        Assertions.assertThat(capturedNews.getId()).isNull();
        Assertions.assertThat(capturedNews.getAuthor().getId()).isEqualTo(newsUpdateRequest.authorId());
        Assertions.assertThat(capturedNews.getTitle()).isEqualTo(newsUpdateRequest.title());
        Assertions.assertThat(capturedNews.getDescription()).isEqualTo(newsUpdateRequest.description());
        Assertions.assertThat(capturedNews.getPicture()).isEqualTo(newsUpdateRequest.picture());
    }

    @Test
    void willThrowNewsNoDataChanes() {
        int id = 3;
        User author = new User();
        author.setId(3);
        News news = new News(author,"title","description","picture");
        when(newsDao.selectNewsById(id)).thenReturn(Optional.of(news));
        NewsUpdateRequest newsUpdateRequest = new NewsUpdateRequest(news.getAuthor().getId(),news.getTitle(),news.getDescription(), news.getPicture());
        when(authorDao.selectUserById(author.getId())).thenReturn(Optional.of(author));
        when(newsDao.existsNewsByTitleAndAuthorId(newsUpdateRequest.title(),newsUpdateRequest.authorId())).thenReturn(false);
        assertThatThrownBy(()->underTest.updateNews(id,newsUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were found");
        verify(newsDao,never()).insertNews(any());
    }
}

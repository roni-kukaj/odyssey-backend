package com.odyssey.news;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.NewsDao;
import com.odyssey.dtos.NewsDto;
import com.odyssey.dtos.NewsRegistrationDto;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.News;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.services.NewsService;
import com.odyssey.services.utils.NewsDtoMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import com.odyssey.models.Role;

import java.nio.file.Path;
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
    @Mock
    private CloudinaryService cloudinaryService;

    private final NewsDtoMapper newsDtoMapper = new NewsDtoMapper();

    private final String FILE_URL = "src/main/resources/images/test.png";
    private Path path;
    private byte[] content;

    private NewsService underTest;

    @BeforeEach
    void setUp(){
        underTest = new NewsService(newsDao, authorDao, cloudinaryService, newsDtoMapper);
    }

    @Test
    void getAllNews() {
        underTest.getAllNews();
        verify(newsDao).selectAllNews();
    }

    @Test
    void getNews() {
        int id = 1;
        News news = new News(
                id, "title", "desc", "pic1", new User(1, "", "", "", "", "", new Role(1, "USER"))
        );
        NewsDto newsDto = newsDtoMapper.apply(news);
        when(newsDao.selectNewsById(id)).thenReturn(Optional.of(news));
        NewsDto news1 = underTest.getNews(id);
        assertThat(news1).isEqualTo(newsDto);
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

        MockMultipartFile image = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                content
        );

        NewsRegistrationDto dto = new NewsRegistrationDto(
                title, description, image, authorId
        );

        when(authorDao.selectUserById(authorId)).thenReturn(Optional.of(author));
        when(newsDao.existsNewsByTitleAndAuthorId(title,authorId)).thenReturn(false);

        underTest.addNews(dto);

        ArgumentCaptor<News> newsArgumentCaptor = ArgumentCaptor.forClass(News.class);
        verify(newsDao).insertNews(newsArgumentCaptor.capture());

        News capturedNews = newsArgumentCaptor.getValue();

        Assertions.assertThat(capturedNews.getId()).isNull();
        Assertions.assertThat(capturedNews.getAuthor().getId()).isEqualTo(dto.authorId());
        Assertions.assertThat(capturedNews.getTitle()).isEqualTo(dto.title());
        Assertions.assertThat(capturedNews.getDescription()).isEqualTo(dto.description());
        Assertions.assertThat(capturedNews.getPicture()).isEqualTo(null);


    }


    @Test
    void willThrowWhenAuthorNotExists() {
//        User author = new User();
//        int authorId = 1;
//        author.setId(authorId);
//        String title = "News title";
//        String description = "Description";
//        String picture = "Picture 2";
//        NewsRegistrationRequest newsRegistrationRequest = new NewsRegistrationRequest(
//                authorId,title,description,picture
//        );
//
//        when(authorDao.selectUserById(authorId)).thenReturn(Optional.empty());
//        lenient().when(newsDao.existsNewsByTitleAndAuthorId(title,authorId)).thenReturn(false);
//        assertThatThrownBy(()->underTest.addNews(newsRegistrationRequest))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessage("author with id [%s] not found".formatted(authorId));
//
//        verify(newsDao,never()).insertNews(any());
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
//        int id = 3;
//        User author = new User();
//        author.setId(1);
//        String title = "Title";
//        String description = "The best";
//        String picture = "Picture 1";
//
//        News news = new News(author,title,description,picture);
//        when(newsDao.selectNewsById(id)).thenReturn(Optional.of(news));
//
//        String newtitle = "New title";
//        String newdescription = "New description";
//        String newpicture = "New picture";
//        User author1 = new User();
//        author1.setId(2);
//
//        NewsUpdateRequest newsUpdateRequest = new NewsUpdateRequest(author1.getId(), newtitle,newdescription, newpicture);
//        when(authorDao.selectUserById(author1.getId())).thenReturn(Optional.of(author1));
//
//        underTest.updateNews(id,newsUpdateRequest);
//
//        ArgumentCaptor<News> newsArgumentCaptor = ArgumentCaptor.forClass(News.class);
//        verify(newsDao).updateNews(newsArgumentCaptor.capture());
//
//        News capturedNews = newsArgumentCaptor.getValue();
//
//        Assertions.assertThat(capturedNews.getId()).isNull();
//        Assertions.assertThat(capturedNews.getAuthor().getId()).isEqualTo(newsUpdateRequest.authorId());
//        Assertions.assertThat(capturedNews.getTitle()).isEqualTo(newsUpdateRequest.title());
//        Assertions.assertThat(capturedNews.getDescription()).isEqualTo(newsUpdateRequest.description());
//        Assertions.assertThat(capturedNews.getPicture()).isEqualTo(newsUpdateRequest.picture());
    }

    @Test
    void willThrowNewsNoDataChanes() {
//        int id = 3;
//        User author = new User();
//        author.setId(3);
//        News news = new News(author,"title","description","picture");
//        when(newsDao.selectNewsById(id)).thenReturn(Optional.of(news));
//        NewsUpdateRequest newsUpdateRequest = new NewsUpdateRequest(news.getAuthor().getId(),news.getTitle(),news.getDescription(), news.getPicture());
//        when(authorDao.selectUserById(author.getId())).thenReturn(Optional.of(author));
//        when(newsDao.existsNewsByTitleAndAuthorId(newsUpdateRequest.title(),newsUpdateRequest.authorId())).thenReturn(false);
//        assertThatThrownBy(()->underTest.updateNews(id,newsUpdateRequest))
//                .isInstanceOf(RequestValidationException.class)
//                .hasMessage("no changes were found");
//        verify(newsDao,never()).insertNews(any());
    }
}

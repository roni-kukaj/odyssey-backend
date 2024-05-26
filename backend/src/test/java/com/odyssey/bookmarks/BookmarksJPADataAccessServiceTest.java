package com.odyssey.bookmarks;

import com.odyssey.models.Location;
import com.odyssey.models.Bookmarks;
import com.odyssey.models.User;
import com.odyssey.repositories.BookmarksRepository;
import com.odyssey.services.data.BookmarksJPADataAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class BookmarksJPADataAccessServiceTest {

    private BookmarksJPADataAccessService bookmarksJPADataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private BookmarksRepository bookmarksRepository;

    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        bookmarksJPADataAccessService = new BookmarksJPADataAccessService(bookmarksRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllBookmarks() {
        bookmarksJPADataAccessService.selectAllBookmarks();
        verify(bookmarksRepository).findAll();
    }

    @Test
    void selectBookmarksById() {
        int id = 1;
        bookmarksJPADataAccessService.selectBookmarksById(id);
        verify(bookmarksRepository).findById(id);
    }

    @Test
    void selectBookmarksByLocationId() {
        int id  = 1;
        bookmarksJPADataAccessService.selectBookmarksByLocationId(id);
        verify(bookmarksRepository).findBookmarksByLocationId(id);
    }

    @Test
    void selectBookmarksByUserId() {
        int id = 1;
        bookmarksJPADataAccessService.selectBookmarksByUserId(id);
        verify(bookmarksRepository).findBookmarksByUserId(id);
    }

    @Test
    void insertBookmarks() {
        Bookmarks bookmarks = new Bookmarks(new Location(),new User());
        bookmarksJPADataAccessService.insertBookmarks(bookmarks);
        verify(bookmarksRepository).save(bookmarks);
    }

    @Test
    void existsBookmarksById() {
        int id = 1;
        bookmarksJPADataAccessService.existsBookmarksById(id);
        verify(bookmarksRepository).existsBookmarksById(id);
    }

    @Test
    void existsBookmarksByLocationIdAndUserId() {
        int locationId = 1;
        int userId = 1;

        bookmarksJPADataAccessService.existsBookmarksByLocationIdAndUserId(locationId, userId);
        verify(bookmarksRepository).existsBookmarksByLocationIdAndUserId(locationId, userId);

    }

    @Test
    void deleteBookmarksById() {
        int id = 1;
        bookmarksJPADataAccessService.deleteBookmarksById(id);
        verify(bookmarksRepository).deleteById(id);
    }
}

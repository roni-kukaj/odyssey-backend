package com.odyssey.bookmarks;


import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.recommendations.Recommendation;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookmarksServiceTest {
    @Mock
    private BookmarksDao bookmarksDao;

    @Mock
    private LocationDao locationDao;

    @Mock
    private UserDao userDao;

    private BookmarksService underTest;

    @BeforeEach
    void setUp(){
        underTest = new BookmarksService(bookmarksDao,locationDao,userDao);
    }

    @Test
    void getAllBookmarks() {
        underTest.getAllBookmarks();
        verify(bookmarksDao).selectAllBookmarks();
    }

    @Test
    void getBookmarks() {
        int id = 1;
        Bookmarks bookmarks = new Bookmarks(id, new Location(), new User());
        when(bookmarksDao.selectBookmarksById(id)).thenReturn(Optional.of(bookmarks));
        Bookmarks bookmarks1 = underTest.getBookmarksById(id);
        assertThat(bookmarks1).isEqualTo(bookmarks);
    }

    @Test
    void getBookmarksByLocationId() {
        Location location = new Location();
        int locationId = 1;
        location.setId(locationId);

        when(locationDao.existsLocationById(locationId)).thenReturn(false);
        assertThatThrownBy(()-> underTest.getBookmarksByLocationId(locationId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(locationId));

        verify(bookmarksDao, never()).insertBookmarks(any());
    }

    @Test
    void getBookmarksByUserId() {
        User user = new User();
        int userId = 1;
        user.setId(userId);

        when(userDao.existsUserById(userId)).thenReturn(false);
        assertThatThrownBy(()-> underTest.getBookmarksByUserId(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(userId));
        verify(bookmarksDao, never()).insertBookmarks(any());
    }

    @Test
    void willThrowWhenGetBookmarksReturnEmptyOptional() {
        int id = 1;
        when(bookmarksDao.selectBookmarksById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.getBookmarksById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("bookmark with id [%s] not found".formatted(id));
    }

    @Test
    void addBookmarks() {
        Location location = new Location();
        User user = new User();

        int locationId = 1;
        int userId = 1;

        location.setId(locationId);
        user.setId(userId);

        BookmarksRegistrationRequest bookmarksRegistrationRequest = new BookmarksRegistrationRequest
                (locationId, userId);

        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));

        when(bookmarksDao.existsBookmarksByLocationIdAndUserId(locationId, userId)).thenReturn(false);

        underTest.addBookmarks(bookmarksRegistrationRequest);

        ArgumentCaptor<Bookmarks> bookmarksArgumentCaptor = ArgumentCaptor.forClass(Bookmarks.class);
        verify(bookmarksDao).insertBookmarks(bookmarksArgumentCaptor.capture());

        Bookmarks capturedBookmarks = bookmarksArgumentCaptor.getValue();

        assertThat(capturedBookmarks.getId()).isNull();
        assertThat(capturedBookmarks.getLocation().getId()).isEqualTo(bookmarksRegistrationRequest.locationId());
        assertThat(capturedBookmarks.getUser().getId()).isEqualTo(bookmarksRegistrationRequest.userId());
    }


    @Test
    void willThrowAddBookmarksLocationNotExists() {
        Location location = new Location();
        User user = new User();

        int locationId = 1;
        int userId = 1;

        location.setId(locationId);
        user.setId(userId);

        BookmarksRegistrationRequest bookmarksRegistrationRequest = new BookmarksRegistrationRequest(
                locationId, userId
        );

        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());

        when(bookmarksDao.existsBookmarksByLocationIdAndUserId(locationId,userId)).thenReturn(false);

        assertThatThrownBy(()->underTest.addBookmarks(bookmarksRegistrationRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(locationId));

        verify(bookmarksDao, never()).insertBookmarks(any());
    }

    @Test
    void willThrowAddBookmarksAlreadyExists() {
        Location location = new Location();
        User user = new User();

        int locationId = 1;
        int userId = 1;

        location.setId(locationId);
        user.setId(userId);

        BookmarksRegistrationRequest bookmarksRegistrationRequest = new BookmarksRegistrationRequest(
                locationId, userId
        );

        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));

        when(bookmarksDao.existsBookmarksByLocationIdAndUserId(locationId, userId)).thenReturn(true);

        assertThatThrownBy(()->underTest.addBookmarks(bookmarksRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("bookmark already exists");

        verify(bookmarksDao, never()).insertBookmarks(any());
    }

    @Test
    void deleteBookmarks() {
        int id = 1;
        when(bookmarksDao.existsBookmarksById(id)).thenReturn(true);
        underTest.deleteBookmarks(id);
        verify(bookmarksDao).deleteBookmarksById(id);
    }

    @Test
    void willThrowDeleteBookmarksNotExists() {
        int id = 1;
        when(bookmarksDao.existsBookmarksById(id)).thenReturn(false);

        assertThatThrownBy(()->underTest.deleteBookmarks(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("bookmark with id [%s] not found".formatted(id));

        verify(bookmarksDao, never()).deleteBookmarksById(any());
    }


    @Test
    void updateBookmarks() {

        int id = 1;
        Location location = new Location();
        User user = new User();

        location.setId(1);
        user.setId(1);

        Bookmarks bookmarks = new Bookmarks(location, user);
        when(bookmarksDao.selectBookmarksById(id)).thenReturn(Optional.of(bookmarks));

        Location location1 = new Location();
        location1.setId(2);

        User user1 = new User();
        user1.setId(2);

        BookmarksUpdateRequest bookmarksUpdateRequest = new BookmarksUpdateRequest(location1.getId(),user1.getId());

        when(locationDao.selectLocationById(location1.getId())).thenReturn(Optional.of(location1));
        when(userDao.selectUserById(user1.getId())).thenReturn(Optional.of(user1));
        underTest.updateBookmarks(id,bookmarksUpdateRequest);

        ArgumentCaptor<Bookmarks> bookmarksArgumentCaptor = ArgumentCaptor.forClass(Bookmarks.class);
        verify(bookmarksDao).updateBookmarks(bookmarksArgumentCaptor.capture());

        Bookmarks capturedBookmarks = bookmarksArgumentCaptor.getValue();

        assertThat(capturedBookmarks.getId()).isNull();
        assertThat(capturedBookmarks.getLocation().getId()).isEqualTo(bookmarksUpdateRequest.locationId());
        assertThat(capturedBookmarks.getUser().getId()).isEqualTo(bookmarksUpdateRequest.userId());
    }


    @Test
    void willThrowUpdateBookmarksNoDataChanes() {

        int id = 2;

        Location location = new Location();
        User user = new User();
        location.setId(2);
        user.setId(2);

        Bookmarks bookmarks = new Bookmarks(location, user);

        when(bookmarksDao.selectBookmarksById(id)).thenReturn(Optional.of(bookmarks));

        BookmarksUpdateRequest bookmarksUpdateRequest = new BookmarksUpdateRequest(
                location.getId(),user.getId()
        );

        when(locationDao.selectLocationById(location.getId())).thenReturn(Optional.of(location));
        when(userDao.selectUserById(user.getId())).thenReturn(Optional.of(user));

        assertThatThrownBy(()-> underTest.updateBookmarks(id, bookmarksUpdateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were found");

        verify(bookmarksDao, never()).updateBookmarks(any());
    }
}

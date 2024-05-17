package com.odyssey.bookmarks;


import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarksService {

    private final BookmarksDao bookmarksDao;
    private final LocationDao locationDao;
    private final UserDao userDao;

    public BookmarksService(BookmarksDao bookmarksDao, LocationDao locationDao, UserDao userDao) {
        this.bookmarksDao = bookmarksDao;
        this.locationDao = locationDao;
        this.userDao = userDao;
    }

    public List<Bookmarks>getAllBookmarks(){
        return bookmarksDao.selectAllBookmarks();
    }

    public Optional<Bookmarks>getBookmarksByLocationId(Integer locationId){
        if(!locationDao.existsLocationById(locationId)){
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return bookmarksDao.selectBookmarksByLocationId(locationId);
    }

    public Optional<Bookmarks>getBookmarksByUserId(Integer userId){
        if(!userDao.existsUserById(userId)){
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return bookmarksDao.selectBookmarksByUserId(userId);
    }

    public Bookmarks getBookmarksById(Integer id){
        return bookmarksDao.selectBookmarksById(id).orElseThrow(()->
                new ResourceNotFoundException("bookmark with id [%s] not found".formatted(id)));
    }

    public void addBookmarks(BookmarksRegistrationRequest bookmarksRegistrationRequest){
        if(bookmarksDao.existsBookmarksByLocationIdAndUserId(
                bookmarksRegistrationRequest.locationId(),bookmarksRegistrationRequest.userId()
        )){
            throw new DuplicateResourceException("bookmark already exists");
        }

        Location location = locationDao.selectLocationById(bookmarksRegistrationRequest.locationId())
                .orElseThrow(() -> new ResourceNotFoundException("location with id [%s] not found".
                        formatted(bookmarksRegistrationRequest.locationId()))
        );

        User user = userDao.selectUserById(bookmarksRegistrationRequest.userId()).orElseThrow(
                () -> new ResourceNotFoundException("user with id [%s] not found"
                        .formatted(bookmarksRegistrationRequest.userId()))
        );

        Bookmarks bookmarks = new Bookmarks(location, user);
        bookmarksDao.insertBookmarks(bookmarks);
    }

    public void deleteBookmarks(Integer id){
        if(bookmarksDao.existsBookmarksById(id)){
            bookmarksDao.deleteBookmarksById(id);
        }
        else {
            throw new ResourceNotFoundException("bookmark with id [%s] not found".formatted(id));
        }
    }
}
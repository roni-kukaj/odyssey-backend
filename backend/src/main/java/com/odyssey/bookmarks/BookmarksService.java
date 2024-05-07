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

    public Optional<Bookmarks>getBookmarksByLocationId(Integer location_id){
        if(!locationDao.existsLocationById(location_id)){
            throw new ResourceNotFoundException("Location with id [%s] not found".formatted(location_id));
        }
        return bookmarksDao.selectBookmarksByLocationId(location_id);

    }


    public Optional<Bookmarks>getBookmarksByUserId(Integer user_id){
        if(!userDao.existsUserById(user_id)){
            throw new ResourceNotFoundException("User with id [%s] not found".formatted(user_id));
        }
        return bookmarksDao.selectBookmarksByUserId(user_id);
    }

    public Bookmarks getBookmarksById(Integer id){
        return bookmarksDao.selectBookmarksById(id).orElseThrow(()->
                new ResourceNotFoundException("Bookmarks with id [%s] not found".formatted(id)));
    }

    public void addBookmarks(BookmarksRegistrationRequest bookmarksRegistrationRequest){
        if(bookmarksDao.existsBookmarksByLocationIdAndUserId(
                bookmarksRegistrationRequest.location_id(),bookmarksRegistrationRequest.user_id()
        )){
            throw new DuplicateResourceException("Bookmarks already exists");
        }

        Location location = locationDao.selectLocationById(bookmarksRegistrationRequest.location_id()).orElseThrow(
                ()-> new ResourceNotFoundException("Location with id [%s] not found".
                        formatted(bookmarksRegistrationRequest.location_id()))
        );

        User user = userDao.selectUserById(bookmarksRegistrationRequest.user_id()).orElseThrow(
                ()->new ResourceNotFoundException("User with id [%s] not found"
                        .formatted(bookmarksRegistrationRequest.user_id()))
        );

        Bookmarks bookmarks = new Bookmarks(location,user);
        bookmarksDao.insertBookmarks(bookmarks);
    }

    public boolean deleteBookmarks(Integer id){
        if(bookmarksDao.existsBookmarksById(id)){
            bookmarksDao.deleteBookmarksById(id);
        }
        else {
            throw new ResourceNotFoundException("Bookmarks with id [%s] not found".formatted(id));
        }
        return false;
    }

    public boolean updateBookmarks(Integer id, BookmarksUpdateRequest bookmarksUpdateRequest){
        Bookmarks existinBookmarks = getBookmarksById(id);

        if(bookmarksDao.existsBookmarksByLocationIdAndUserId
                (bookmarksUpdateRequest.location_id(),bookmarksUpdateRequest.user_id())){
            throw new DuplicateResourceException("Bookmarks already exists");
        }

        Location location = locationDao.selectLocationById(bookmarksUpdateRequest.location_id()).orElseThrow(
                ()-> new ResourceNotFoundException("Location with id [%s] not found".formatted(bookmarksUpdateRequest.location_id()))
        );

        User user = userDao.selectUserById(bookmarksUpdateRequest.user_id()).orElseThrow(
                ()-> new ResourceNotFoundException("User with id [%s] not found".formatted(bookmarksUpdateRequest.user_id()))
        );

        boolean changes = false;

        if(bookmarksUpdateRequest.location_id()!=null && !bookmarksUpdateRequest.location_id().equals(existinBookmarks.getLocation().getId())){
            existinBookmarks.setLocation(location);
            changes = true;
        }


        if(bookmarksUpdateRequest.user_id()!=null && !bookmarksUpdateRequest.user_id().equals(existinBookmarks.getUser().getId())){
            existinBookmarks.setUser(user);
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no changes were found");
        }

        bookmarksDao.updateBookmarks(existinBookmarks);
        return changes;

    }

    }




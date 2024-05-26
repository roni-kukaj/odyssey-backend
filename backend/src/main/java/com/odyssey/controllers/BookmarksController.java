package com.odyssey.controllers;

import com.odyssey.dtos.BookmarksDto;
import com.odyssey.dtos.BookmarksRegistrationRequest;
import com.odyssey.services.BookmarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarksController {

    @Autowired
    private BookmarksService bookmarksService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @GetMapping
    public List<BookmarksDto> getAllBookmarks(){
        return bookmarksService.getAllBookmarks();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @GetMapping("/{bookmarksId}")
    public BookmarksDto getBookmarksById(@PathVariable("bookmarksId") Integer bookmarksId){
        return bookmarksService.getBookmarksById(bookmarksId);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public List<BookmarksDto> getBookmarksByUserId(@PathVariable("userId") Integer userId){
        return bookmarksService.getBookmarksByUserId(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @GetMapping("/location/{locationId}")
    public Optional<BookmarksDto> getBookmarksByLocationId(@PathVariable("locationId") Integer locationId){
        return bookmarksService.getBookmarksByLocationId(locationId);
    }

    @PreAuthorize("hasAuthority('USER') and #request.userId == authentication.principal.id")
    @PostMapping
    public void registerBookmarks(@RequestBody BookmarksRegistrationRequest request){
        bookmarksService.addBookmarks(request);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{bookmarksId}")
    public void deleteBookmarks(@PathVariable("bookmarksId") Integer bookmarksId){
        bookmarksService.deleteBookmarks(bookmarksId);
    }
}

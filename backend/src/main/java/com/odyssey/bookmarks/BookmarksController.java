package com.odyssey.bookmarks;

import com.odyssey.recommendations.Recommendation;
import com.odyssey.recommendations.RecommendationRegistrationRequest;
import com.odyssey.recommendations.RecommendationUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bookmarks")
public class BookmarksController {

    @Autowired
    private BookmarksService bookmarksService;

    @GetMapping
    public List<Bookmarks> getAllBookmarks(){
        return bookmarksService.getAllBookmarks();
    }

    @GetMapping("/{bookmarksId}")
    public Bookmarks getBookmarksById(@PathVariable("bookmarksId") Integer bookmarksId){
        return bookmarksService.getBookmarksById(bookmarksId);
    }

    @GetMapping("/user/{userId}")
    public Optional<Bookmarks> getBookmarksByUserId(@PathVariable("userId") Integer userId){
        return bookmarksService.getBookmarksByUserId(userId);
    }

    @GetMapping("/location/{locationId}")
    public Optional<Bookmarks> getBookmarksByLocationId(@PathVariable("locationId") Integer locationId){
        return bookmarksService.getBookmarksByLocationId(locationId);
    }

    @PostMapping
    public void registerBookmarks(@RequestBody BookmarksRegistrationRequest request){
        bookmarksService.addBookmarks(request);
    }

    @DeleteMapping("/{bookmarksId}")
    public void deleteBookmarks(@PathVariable("bookmarksId") Integer bookmarksId){
        bookmarksService.deleteBookmarks(bookmarksId);
    }

    @PutMapping("/{bookmarksId}")
    public void updateBookmarks(@PathVariable("bookmarksId") Integer bookmarksId, @RequestBody BookmarksUpdateRequest request){
        bookmarksService.updateBookmarks(bookmarksId, request);
    }


}

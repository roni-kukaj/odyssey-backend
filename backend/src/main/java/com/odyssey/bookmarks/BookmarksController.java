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

    @GetMapping("/{bookmarksid}")
    public Bookmarks getBookmarksById(@PathVariable("bookmarksid") Integer bookmarks_id){
        return bookmarksService.getBookmarksById(bookmarks_id);
    }

    @GetMapping("/user/{userid}")
    public Optional<Bookmarks> getBookmarksByUserId(@PathVariable("userid") Integer user_id){
        return bookmarksService.getBookmarksByUserId(user_id);
    }

    @GetMapping("/location/{locationid}")
    public Optional<Bookmarks> getBookmarksByLocationId(@PathVariable("locationid") Integer location_id){
        return bookmarksService.getBookmarksByLocationId(location_id);
    }

    @PostMapping
    public void registerBookmarks(@RequestBody BookmarksRegistrationRequest request){
        bookmarksService.addBookmarks(request);
    }

    @DeleteMapping("/{bookmarksid}")
    public void deleteBookmarks(@PathVariable("bookmarksid") Integer bookmarks_id){
        bookmarksService.deleteBookmarks(bookmarks_id);
    }



    @PutMapping("/{bookmarksid}")
    public void updateBookmarks(@PathVariable("bookmarksid") Integer bookmarks_id, @RequestBody BookmarksUpdateRequest request){
        bookmarksService.updateBookmarks(bookmarks_id,request);
    }


}

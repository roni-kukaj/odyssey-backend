package com.odyssey.bookmarks;


import java.util.List;
import java.util.Optional;

public interface BookmarksDao {
    List<Bookmarks>selectAllBookmarks();
    Optional<Bookmarks>selectBookmarksById(Integer id);
    Optional<Bookmarks>selectBookmarksByLocationId(Integer location_id);
    Optional<Bookmarks>selectBookmarksByUserId(Integer user_id);
    void insertBookmarks(Bookmarks bookmarks);
    void updateBookmarks(Bookmarks bookmarks);
    void deleteBookmarksById(Integer id);
    boolean existsBookmarksById(Integer id);
    boolean existsBookmarksByLocationIdAndUserId(Integer location_id, Integer user_id);
}

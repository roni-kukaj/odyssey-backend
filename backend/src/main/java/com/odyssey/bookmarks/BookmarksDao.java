package com.odyssey.bookmarks;


import java.util.List;
import java.util.Optional;

public interface BookmarksDao {
    List<Bookmarks> selectAllBookmarks();
    Optional<Bookmarks> selectBookmarksById(Integer id);
    Optional<Bookmarks> selectBookmarksByLocationId(Integer locationId);
    Optional<Bookmarks> selectBookmarksByUserId(Integer userId);
    void insertBookmarks(Bookmarks bookmarks);
    void updateBookmarks(Bookmarks bookmarks);
    void deleteBookmarksById(Integer id);
    boolean existsBookmarksById(Integer id);
    boolean existsBookmarksByLocationIdAndUserId(Integer locationId, Integer userId);
}

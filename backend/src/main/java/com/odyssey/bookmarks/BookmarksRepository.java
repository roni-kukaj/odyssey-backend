package com.odyssey.bookmarks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.Optional;

public interface BookmarksRepository extends JpaRepository<Bookmarks,Integer> {
    boolean existsBookmarksById(Integer id);
    boolean existsBookmarksByLocationIdAndUserId(Integer locationId, Integer userId);
    Optional<Bookmarks>findBookmarksByLocationId(Integer locationId);
    Optional<Bookmarks>findBookmarksByUserId(Integer userId);

}

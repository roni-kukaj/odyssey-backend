package com.odyssey.bookmarks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.Optional;

public interface BookmarksRepository extends JpaRepository<Bookmarks,Integer> {
    boolean existsBookmarksById(Integer id);
    boolean existsBookmarksByLocationIdAndUserId(Integer location_id, Integer user_id);
    Optional<Bookmarks>findBookmarksByLocationId(Integer location_id);
    Optional<Bookmarks>findBookmarksByUserId(Integer user_id);

}

package com.odyssey.repositories;

import com.odyssey.models.Bookmarks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarksRepository extends JpaRepository<Bookmarks,Integer> {
    boolean existsBookmarksById(Integer id);
    boolean existsBookmarksByLocationIdAndUserId(Integer locationId, Integer userId);
    Optional<Bookmarks>findBookmarksByLocationId(Integer locationId);
    List<Bookmarks> findBookmarksByUserId(Integer userId);

}

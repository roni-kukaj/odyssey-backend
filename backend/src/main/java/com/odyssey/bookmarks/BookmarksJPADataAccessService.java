package com.odyssey.bookmarks;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("bookmarksJPARepository")
public class BookmarksJPADataAccessService implements BookmarksDao{
    private final BookmarksRepository bookmarksRepository;

    public BookmarksJPADataAccessService(BookmarksRepository bookmarksRepository) {
        this.bookmarksRepository = bookmarksRepository;
    }

    @Override
    public List<Bookmarks> selectAllBookmarks() {
        return bookmarksRepository.findAll();
    }

    @Override
    public Optional<Bookmarks> selectBookmarksById(Integer id) {
        return bookmarksRepository.findById(id);
    }

    @Override
    public Optional<Bookmarks> selectBookmarksByLocationId(Integer locationId) {
        return bookmarksRepository.findBookmarksByLocationId(locationId);
    }

    @Override
    public Optional<Bookmarks> selectBookmarksByUserId(Integer userId) {
        return bookmarksRepository.findBookmarksByUserId(userId);
    }

    @Override
    public void insertBookmarks(Bookmarks bookmarks) {
        bookmarksRepository.save(bookmarks);
    }

    @Override
    public void updateBookmarks(Bookmarks bookmarks) {
        bookmarksRepository.save(bookmarks);
    }

    @Override
    public void deleteBookmarksById(Integer id) {
        bookmarksRepository.deleteById(id);
    }

    @Override
    public boolean existsBookmarksById(Integer id) {
        return  bookmarksRepository.existsBookmarksById(id);
    }

    @Override
    public boolean existsBookmarksByLocationIdAndUserId(Integer locationId, Integer userId) {
        return bookmarksRepository.existsBookmarksByLocationIdAndUserId(locationId, userId);
    }
}

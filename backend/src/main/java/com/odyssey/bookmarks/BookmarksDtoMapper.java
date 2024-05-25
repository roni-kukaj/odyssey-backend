package com.odyssey.bookmarks;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class BookmarksDtoMapper implements Function<Bookmarks, BookmarksDto> {
    @Override
    public BookmarksDto apply(Bookmarks bookmarks) {
        return new BookmarksDto(
                bookmarks.getId(),
                bookmarks.getLocation(),
                bookmarks.getUser().getId(),
                bookmarks.getUser().getUsername()
        );
    }
}

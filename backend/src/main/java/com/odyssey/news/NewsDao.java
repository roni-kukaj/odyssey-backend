package com.odyssey.news;

import java.util.List;
import java.util.Optional;

public interface NewsDao {
    List<News> selectAllNews();
    Optional<News> selectNewsById(Integer id);
    List<News>selectNewsByAuthorId( Integer authorId);
    void insertNews(News news);
    void updateNews(News news);
    void deleteNewsById(Integer id);
    boolean existsNewsById(Integer id);
    boolean existsNewsByTitleAndAuthorId(String title, Integer authorId);

}

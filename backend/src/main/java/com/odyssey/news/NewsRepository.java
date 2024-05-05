package com.odyssey.news;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Integer> {
    boolean existsNewsById(Integer id);
    boolean existsNewsByTitleAndAuthorId(String title,Integer author_id);
    List<News> findNewsByAuthorId(Integer author_id);
}

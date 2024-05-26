package com.odyssey.services.data;

import com.odyssey.daos.NewsDao;
import com.odyssey.models.News;
import com.odyssey.repositories.NewsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("newsJPAService")
public class NewsJPADataAccessService implements NewsDao {

    private final NewsRepository newsRepository;

    public NewsJPADataAccessService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;

    }

    @Override
    public List<News> selectAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public Optional<News> selectNewsById(Integer id) {
        return newsRepository.findById(id);
    }

    @Override
    public List<News> selectNewsByAuthorId(Integer authorId) {
        return newsRepository.findNewsByAuthorId(authorId);
    }

    @Override
    public void insertNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public void updateNews(News news) {
        newsRepository.save(news);

    }

    @Override
    public void deleteNewsById(Integer id) {
        newsRepository.deleteById(id);

    }

    @Override
    public boolean existsNewsById(Integer id) {
        return newsRepository.existsNewsById(id);
    }

    @Override
    public boolean existsNewsByTitleAndAuthorId(String title, Integer author_id) {
        return newsRepository.existsNewsByTitleAndAuthorId(title, author_id);
    }
}
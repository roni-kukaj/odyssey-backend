package com.odyssey.news;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    private final NewsDao newsDao;
    private final UserDao authorDao;

    public NewsService(NewsDao newsDao, UserDao authorDao) {
        this.newsDao = newsDao;
        this.authorDao = authorDao;
    }

    public List<News> getAllNews(){
        return newsDao.selectAllNews();

    }

    public News getNews(Integer id){
        return newsDao.selectNewsById(id).orElseThrow(()->
                new ResourceNotFoundException("news with id [%s] not found".formatted(id)));
    }

    public List<News>getNewsByAuthorId(Integer authorId){
        if(!authorDao.existsUserById(authorId)){
            throw new ResourceNotFoundException("author with id [%s] not found".formatted(authorId));
        }
        return newsDao.selectNewsByAuthorId(authorId);
    }

    public void addNews(NewsRegistrationRequest newsRegistrationRequest){
        if(newsDao.existsNewsByTitleAndAuthorId(newsRegistrationRequest.title(),newsRegistrationRequest.authorId())){
            throw new DuplicateResourceException("news already exists");
        }
        User author = authorDao.selectUserById(newsRegistrationRequest.authorId())
                .orElseThrow(()->
                        new ResourceNotFoundException
                                ("author with id [%s] not found".formatted(newsRegistrationRequest.authorId())));

        News news = new News(author, newsRegistrationRequest.title(), newsRegistrationRequest.description(), newsRegistrationRequest.picture());
        newsDao.insertNews(news);
    }

    public boolean deleteNews(Integer id){
        if(newsDao.existsNewsById(id)){
            newsDao.deleteNewsById(id);
        }
        else {
            throw new ResourceNotFoundException("news with id [%s] not found".formatted(id));
        }
        return false;
    }


    public boolean updateNews(Integer id, NewsUpdateRequest newsUpdateRequest){
        News existingNews = getNews(id);

        if(newsDao.existsNewsByTitleAndAuthorId(newsUpdateRequest.title(),newsUpdateRequest.authorId())){
            throw new DuplicateResourceException("news already exists");
        }

        User author = authorDao.selectUserById(newsUpdateRequest.authorId()).orElseThrow(
                ()-> new ResourceNotFoundException("author with id [%s] not found".formatted(newsUpdateRequest.authorId()))
        );


        boolean changes = false;

        if(newsUpdateRequest.title()!=null && !newsUpdateRequest.title().equals(existingNews.getTitle())){
            existingNews.setTitle(newsUpdateRequest.title());
            changes = true;
        }

        if(newsUpdateRequest.description()!=null && !newsUpdateRequest.description().equals(existingNews.getDescription())){
            existingNews.setDescription(newsUpdateRequest.description());
            changes = true;
        }


        if(newsUpdateRequest.picture()!=null && !newsUpdateRequest.picture().equals(existingNews.getPicture())){
            existingNews.setPicture(newsUpdateRequest.picture());
            changes = true;
        }

        if(newsUpdateRequest.authorId()!=null && !newsUpdateRequest.authorId().equals(existingNews.getAuthor().getId())){
            existingNews.setAuthor(author);
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no changes were found");
        }

        newsDao.updateNews(existingNews);
        return changes;

    }
}
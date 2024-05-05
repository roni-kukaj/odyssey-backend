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
                new ResourceNotFoundException("News with id [%s] not found".formatted(id)));
    }

    public List<News>getNewsByAuthorId(Integer author_id){
        if(!authorDao.existsUserById(author_id)){
            throw new ResourceNotFoundException("Author with id [%s] not found".formatted(author_id));
        }
        return newsDao.selectNewsByAuthorId(author_id);
    }

    public void addNews(NewsRegistrationRequest newsRegistrationRequest){
        if(newsDao.existsNewsByTitleAndAuthorId(newsRegistrationRequest.title(),newsRegistrationRequest.author_id())){
            throw new DuplicateResourceException("News already exists");
        }
        User author = authorDao.selectUserById(newsRegistrationRequest.author_id())
                .orElseThrow(()->
                        new ResourceNotFoundException
                                ("Author with id [%s] not found".formatted(newsRegistrationRequest.author_id())));

        News news = new News(author,newsRegistrationRequest.title(),newsRegistrationRequest.description(),newsRegistrationRequest.picture() );
        newsDao.insertNews(news);
    }

    public boolean deleteNews(Integer id){
        if(newsDao.existsNewsById(id)){
            newsDao.deleteNewsById(id);
        }
        else {
            throw new ResourceNotFoundException("News with id [%s] not found".formatted(id));
        }
        return false;
    }


    public boolean updateNews(Integer id, NewsUpdateRequest newsUpdateRequest){
        News existingNews = getNews(id);

        if(newsDao.existsNewsByTitleAndAuthorId(newsUpdateRequest.title(),newsUpdateRequest.author_id())){
            throw new DuplicateResourceException("News already exists");
        }

        User author = authorDao.selectUserById(newsUpdateRequest.author_id()).orElseThrow(
                ()-> new ResourceNotFoundException("Author with id [%s] not found".formatted(newsUpdateRequest.author_id()))
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

        if(newsUpdateRequest.author_id()!=null && !newsUpdateRequest.author_id().equals(existingNews.getAuthor().getId())){
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
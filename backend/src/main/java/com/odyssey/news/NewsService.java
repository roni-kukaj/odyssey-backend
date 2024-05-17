package com.odyssey.news;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.events.Event;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.fileService.FileService;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class NewsService {
    private final NewsDao newsDao;
    private final UserDao authorDao;
    private final CloudinaryService cloudinaryService;

    public NewsService(NewsDao newsDao, UserDao authorDao, CloudinaryService cloudinaryService) {
        this.newsDao = newsDao;
        this.authorDao = authorDao;
        this.cloudinaryService = cloudinaryService;
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

    public void addNews(NewsRegistrationDto dto) {
        if (newsDao.existsNewsByTitleAndAuthorId(dto.title(), dto.authorId())){
            throw new DuplicateResourceException("news already exists");
        }
        User author = authorDao.selectUserById(dto.authorId())
            .orElseThrow(()->
                new ResourceNotFoundException
                    ("user with id [%s] not found".formatted(dto.authorId())));

        File file = FileService.convertFile(dto.image());

        try {
            String url = cloudinaryService.uploadImage(file, "news");
            News news = new News (
                    dto.title(), dto.description(), url, author
            );
            newsDao.insertNews(news);
        } catch (IOException e) {
            // TODO -> tell the user something
        }
    }

    public void addNews(NewsRegistrationRequest newsRegistrationRequest){
        if(newsDao.existsNewsByTitleAndAuthorId(newsRegistrationRequest.title(),newsRegistrationRequest.authorId())){
            throw new DuplicateResourceException("news already exists");
        }
        User author = authorDao.selectUserById(newsRegistrationRequest.authorId())
                .orElseThrow(()->
                        new ResourceNotFoundException
                                ("author with id [%s] not found".formatted(newsRegistrationRequest.authorId())));

        News news = new News(newsRegistrationRequest.title(), newsRegistrationRequest.description(), newsRegistrationRequest.picture(), author);
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

    public void updateNewsInformation(Integer id, NewsUpdateInformationDto dto) {
        News existingNews = getNews(id);

        if (newsDao.existsNewsByTitleAndAuthorId(dto.title(), existingNews.getAuthor().getId())){
            throw new DuplicateResourceException("news already exists");
        }

        boolean changes = false;

        if (dto.title() != null && !dto.title().equals(existingNews.getTitle())){
            existingNews.setTitle(dto.title());
            changes = true;
        }
        if (dto.description() != null && !dto.description().equals(existingNews.getDescription())){
            existingNews.setDescription(dto.description());
            changes = true;
        }
        if (!changes) {
            throw new RequestValidationException("no changes were found");
        }
        newsDao.updateNews(existingNews);
    }

    public void updateNewsPicture(Integer id, MultipartFile image) {
        News existingNews = getNews(id);
        try {
            File file = FileService.convertFile(image);
            String newUrl = cloudinaryService.uploadImage(file, "news");
            if (cloudinaryService.deleteImageByUrl(existingNews.getPicture())) {
                existingNews.setPicture(newUrl);
                newsDao.updateNews(existingNews);
            }
            else {
                throw new IOException();
            }
        } catch (IOException e) {
            // TODO -> tell the user something
        }
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
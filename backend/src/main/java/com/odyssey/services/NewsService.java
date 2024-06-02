package com.odyssey.services;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.NewsDao;
import com.odyssey.dtos.NewsDto;
import com.odyssey.dtos.NewsRegistrationDto;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.exception.UnprocessableEntityException;
import com.odyssey.services.file.FileService;
import com.odyssey.models.News;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.dtos.NewsUpdateDto;
import com.odyssey.services.utils.NewsDtoMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NewsDao newsDao;
    private final UserDao authorDao;
    private final CloudinaryService cloudinaryService;
    private final NewsDtoMapper newsDtoMapper;

    public NewsService(NewsDao newsDao, UserDao authorDao, CloudinaryService cloudinaryService, NewsDtoMapper newsDtoMapper) {
        this.newsDao = newsDao;
        this.authorDao = authorDao;
        this.cloudinaryService = cloudinaryService;
        this.newsDtoMapper = newsDtoMapper;
    }

    public List<NewsDto> getAllNews(){
        return newsDao.selectAllNews()
                .stream()
                .map(newsDtoMapper)
                .collect(Collectors.toList());
    }

    public NewsDto getNews(Integer id){
        return newsDao.selectNewsById(id)
                .map(newsDtoMapper)
                .orElseThrow(()->
                new ResourceNotFoundException("news with id [%s] not found".formatted(id)));
    }

    private News getNewsById(Integer id) {
        return newsDao.selectNewsById(id).orElseThrow(()->
                new ResourceNotFoundException("news with id [%s] not found".formatted(id)));
    }

    public List<NewsDto>getNewsByAuthorId(Integer authorId){
        if(!authorDao.existsUserById(authorId)){
            throw new ResourceNotFoundException("author with id [%s] not found".formatted(authorId));
        }
        return newsDao.selectNewsByAuthorId(authorId)
                .stream().map(newsDtoMapper).collect(Collectors.toList());
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
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void deleteNews(Integer id){
        if(newsDao.existsNewsById(id)){
            newsDao.deleteNewsById(id);
        }
        else {
            throw new ResourceNotFoundException("news with id [%s] not found".formatted(id));
        }
    }

    public void updateNews(Integer id, NewsUpdateDto dto) {
        News existingNews = getNewsById(id);

        if (newsDao.existsNewsByTitleAndAuthorId(dto.title(), existingNews.getAuthor().getId())){
            throw new DuplicateResourceException("news already exists");
        }

        boolean changes = false;

        if (dto.title() != null && !dto.title().equals(existingNews.getTitle())){
            existingNews.setTitle(dto.title());
            changes = true;
        }
        if (dto.description() != null && !dto.description().equals(existingNews.getDescription())) {
            existingNews.setDescription(dto.description());
            changes = true;
        }

        try {
            if (dto.file() != null) {
                File file = FileService.convertFile(dto.file());
                String newUrl = cloudinaryService.uploadImage(file, "news");
                cloudinaryService.deleteImageByUrl(existingNews.getPicture());
                existingNews.setPicture(newUrl);
                changes = true;
            }

        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }

        if (!changes) {
            throw new RequestValidationException("no changes were found");
        }

        newsDao.updateNews(existingNews);
    }
}
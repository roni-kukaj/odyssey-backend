package com.odyssey.controllers;

import com.odyssey.dtos.NewsDto;
import com.odyssey.dtos.NewsRegistrationDto;
import com.odyssey.services.NewsService;
import com.odyssey.dtos.NewsUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public List<NewsDto> getAllNews(){
        return newsService.getAllNews();
    }

    @GetMapping("/{newsId}")
    public NewsDto getNewsById(@PathVariable("newsId") Integer newsId){
        return newsService.getNews(newsId);
    }

    @GetMapping("/user/{authorId}")
    public List<NewsDto> getNewsByAuthorId(@PathVariable("authorId") Integer authorId){
        return newsService.getNewsByAuthorId(authorId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void addNews(@ModelAttribute NewsRegistrationDto dto){
        newsService.addNews(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/{newsId}")
    public void deleteNews(@PathVariable("newsId") Integer newsId){
        newsService.deleteNews(newsId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @PutMapping("/{newsId}")
    public void updateNewsInformation(
            @PathVariable("newsId") Integer newsId,
            @ModelAttribute NewsUpdateDto dto){
        newsService.updateNews(newsId, dto);
    }
}

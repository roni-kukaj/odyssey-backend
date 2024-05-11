package com.odyssey.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public List<News> getAllNews(){
        return newsService.getAllNews();
    }

    @GetMapping("/{newsId}")
    public News GetNewsById(@PathVariable("newsId") Integer newsId){
        return newsService.getNews(newsId);
    }

    @GetMapping("/user/{authorId}")
    public List<News>getNewsByAuthorId(@PathVariable("authorId") Integer authorId){
        return newsService.getNewsByAuthorId(authorId);
    }

    @PostMapping
    public void addReview(@RequestBody NewsRegistrationRequest newsRegistrationRequest){
        newsService.addNews(newsRegistrationRequest);
    }

    @DeleteMapping("/{newsId}")
    public void deleteReview(@PathVariable("newsId") Integer newsId){
        newsService.deleteNews(newsId);
    }


    @PutMapping("/{newsId}")
    public void updateReview(@PathVariable("newsId") Integer newsId, @RequestBody NewsUpdateRequest newsUpdateRequest){
        newsService.updateNews(newsId,newsUpdateRequest);
    }
}

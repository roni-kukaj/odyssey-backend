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

    @GetMapping("/{newsid}")
    public News GetNewsById(@PathVariable("newsid") Integer newsId){
        return newsService.getNews(newsId);
    }

    @GetMapping("/user/{authorid}")
    public List<News>getNewsByAuthorId(@PathVariable("authorid") Integer authorId){
        return newsService.getNewsByAuthorId(authorId);
    }

    @PostMapping
    public void addReview(@RequestBody NewsRegistrationRequest newsRegistrationRequest){
        newsService.addNews(newsRegistrationRequest);
    }

    @DeleteMapping("/{newsid}")
    public void deleteReview(@PathVariable("newsid") Integer newsId){
        newsService.deleteNews(newsId);
    }


    @PutMapping("/{newsid}")
    public void updateReview(@PathVariable("newsid") Integer newsId, @RequestBody NewsUpdateRequest newsUpdateRequest){
        newsService.updateNews(newsId,newsUpdateRequest);
    }
}

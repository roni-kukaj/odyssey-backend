package com.odyssey.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    public News getNewsById(@PathVariable("newsId") Integer newsId){
        return newsService.getNews(newsId);
    }

    @GetMapping("/user/{authorId}")
    public List<News> getNewsByAuthorId(@PathVariable("authorId") Integer authorId){
        return newsService.getNewsByAuthorId(authorId);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void addNews(
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestPart("image") MultipartFile image,
        @RequestParam("authorId") Integer authorId
    ){
        newsService.addNews(new NewsRegistrationDto(
            title, description, image, authorId
        ));
    }

    @DeleteMapping("/{newsId}")
    public void deleteNews(@PathVariable("newsId") Integer newsId){
        newsService.deleteNews(newsId);
    }


    @PutMapping("/{newsId}")
    public void updateNewsInformation(
            @PathVariable("newsId") Integer newsId,
            @RequestBody NewsUpdateInformationDto dto){
        newsService.updateNewsInformation(newsId, dto);
    }

    @PutMapping(value = "/{newsId}/picture", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public void updateNewsPicture(
            @PathVariable("newsId") Integer newsId,
            @RequestPart("file") MultipartFile file
    ) {
        newsService.updateNewsPicture(newsId, file);
    }

}

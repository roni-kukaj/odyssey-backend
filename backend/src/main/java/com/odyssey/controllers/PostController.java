package com.odyssey.controllers;
import com.odyssey.dtos.PostDto;
import com.odyssey.dtos.PostRegistrationDto;
import com.odyssey.services.PostService;
import com.odyssey.dtos.PostUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/user/{userId}")
    public List<PostDto> getPostsByUserId(@PathVariable("userId") Integer userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable("postId") Integer postId) {
        return postService.getPost(postId);
    }

    @PreAuthorize("hasAuthority('USER') and #dto.userId == authentication.principal.id")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void registerPost(@ModelAttribute PostRegistrationDto dto) {
        postService.addPost(dto);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MAINADMIN')")
    @DeleteMapping("/user/{userId}")
    public void deletePostsByUserId(@PathVariable("userId") Integer userId) {
        postService.deletePostsByUserId(userId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{postId}")
    public void updatePostInformation(
            @PathVariable("postId") Integer postId,
            @ModelAttribute PostUpdateDto dto) {
        postService.updatePost(postId, dto);
    }
}

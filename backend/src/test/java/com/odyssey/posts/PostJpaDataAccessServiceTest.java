package com.odyssey.posts;

import com.odyssey.models.Post;
import com.odyssey.repositories.PostRepository;
import com.odyssey.services.data.PostJPADataAccessService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


public class PostJpaDataAccessServiceTest {
    private PostJPADataAccessService postDataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        postDataAccessService = new PostJPADataAccessService(postRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllPosts() {
        postDataAccessService.selectAllPosts();
        verify(postRepository).findAll();
    }

    @Test
    void selectPostById() {
        int id = 1;
        postDataAccessService.selectPostById(id);
        verify(postRepository).findById(id);
    }

    @Test
    void selectPostsByUserId() {
        int userId = 1;
        postDataAccessService.selectPostsByUserId(userId);
        verify(postRepository).findPostsByUserId(userId);
    }

    @Test
    void insertPost() {
        Post post = new Post();
        postDataAccessService.insertPost(post);
        verify(postRepository).save(post);
    }

    @Test
    void deletePostById() {
        int id = 1;
        postDataAccessService.deletePostById(id);
        verify(postRepository).deleteById(id);
    }

    @Test
    void deletePostByUserId() {
        int id = 1;
        postDataAccessService.deletePostsByUserId(id);
        verify(postRepository).deletePostsByUserId(id);
    }

    @Test
    void existsPostById() {
        int id = 1;
        postDataAccessService.existsPostById(id);
        verify(postRepository).existsById(id);
    }

    @Test
    void existsPostByUserIdAndTripId() {
        int userId = 1;
        int tripId = 1;
        postDataAccessService.existsPostByUserIdAndTripId(userId, tripId);
        verify(postRepository).existsPostByUserIdAndTripId(userId, tripId);
    }

    @Test
    void updatePost() {
        Post post = new Post();
        postDataAccessService.updatePost(post);
        verify(postRepository).save(post);
    }

}

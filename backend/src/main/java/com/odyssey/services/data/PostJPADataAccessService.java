package com.odyssey.services.data;

import com.odyssey.daos.PostDao;
import com.odyssey.models.Post;
import com.odyssey.repositories.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("postJPAService")
public class PostJPADataAccessService implements PostDao {

    private final PostRepository postRepository;

    public PostJPADataAccessService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> selectAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> selectPostsByUserId(Integer userId) {
        return postRepository.findPostsByUserId(userId);
    }

    @Override
    public Optional<Post> selectPostById(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public void insertPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePostById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public void deletePostsByUserId(Integer userId) {
        postRepository.deletePostsByUserId(userId);
    }

    @Override
    public void updatePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public boolean existsPostById(Integer id) {
        return postRepository.existsById(id);
    }

    @Override
    public boolean existsPostByUserIdAndTripId(Integer userId, Integer tripId) {
        return postRepository.existsPostByUserIdAndTripId(userId, tripId);
    }
}

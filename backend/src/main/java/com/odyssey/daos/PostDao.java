package com.odyssey.daos;

import com.odyssey.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    List<Post> selectAllPosts();
    List<Post> selectPostsByUserId(Integer userId);
    Optional<Post> selectPostById(Integer id);
    void insertPost(Post post);
    void deletePostById(Integer id);
    void deletePostsByUserId(Integer userId);
    void updatePost(Post post);
    boolean existsPostById(Integer id);
    boolean existsPostByUserIdAndTripId(Integer userId, Integer tripId);
}

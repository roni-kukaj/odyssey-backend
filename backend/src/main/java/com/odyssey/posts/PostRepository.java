package com.odyssey.posts;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findPostsByUserId(Integer userId);
    Optional<Post> findPostByUserIdAndTripId(Integer userId, Integer tripId);
    boolean existsPostByUserIdAndTripId(Integer userId, Integer tripId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p.user.id = :userId")
    void deletePostsByUserId(Integer userId);
}

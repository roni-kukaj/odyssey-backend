package com.odyssey.posts;

import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.plans.Plan;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostDao postDao;
    private final UserDao userDao;
    private final TripDao tripDao;

    public PostService(
            @Qualifier("postJPAService") PostDao postDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("tripJPAService") TripDao tripDao
    ) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tripDao = tripDao;
    }

    public List<Post> getAllPosts() {
        return postDao.selectAllPosts();
    }

    public List<Post> getPostsByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)){
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return postDao.selectPostsByUserId(userId);
    }

    public Post getPost(Integer id) {
        return postDao.selectPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post with id [%s] not found".formatted(id)));
    }

    public void addPost(PostRegistrationRequest request) {
        User user = userDao.selectUserById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(request.userId())));

        Trip trip = tripDao.selectTripById(request.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(request.tripId())));

        LocalDate postedTime = LocalDate.now();
        Post post = new Post(request.text(), request.image(), postedTime, user, trip);

        if (postDao.existsPostByUserIdAndTripId(request.userId(), request.tripId())) {
            throw new DuplicateResourceException("a trip can only have one post");
        }

        postDao.insertPost(post);
    }

    public boolean deletePost(Integer id) {
        if (postDao.existsPostById(id)) {
            postDao.deletePostById(id);
        }
        else {
            throw new ResourceNotFoundException("post with id [%s] not found".formatted(id));}
        return false;
    }

    public boolean deletePostsByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        postDao.deletePostsByUserId(userId);
        return false;
    }

    public boolean updatePost(Integer id, PostUpdateRequest request) {
        Post existingPost = getPost(id);

        boolean changes = false;

        if (request.text() != null && !request.text().equals(existingPost.getText())) {
            existingPost.setText(request.text());
            changes = true;
        }
        if (request.image() != null && !request.image().equals(existingPost.getImage())) {
            existingPost.setImage(request.image());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        postDao.updatePost(existingPost);
        return changes;

    }

}

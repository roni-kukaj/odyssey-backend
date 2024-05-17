package com.odyssey.posts;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.events.Event;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.exception.UnprocessableEntityException;
import com.odyssey.fileService.FileService;
import com.odyssey.plans.Plan;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostDao postDao;
    private final UserDao userDao;
    private final TripDao tripDao;
    private final CloudinaryService cloudinaryService;

    public PostService(
            @Qualifier("postJPAService") PostDao postDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("tripJPAService") TripDao tripDao,
            CloudinaryService cloudinaryService
    ) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tripDao = tripDao;
        this.cloudinaryService = cloudinaryService;
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

    public void addPost(PostRegistrationDto dto) {
        User user = userDao.selectUserById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(dto.userId())));

        Trip trip = tripDao.selectTripById(dto.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(dto.tripId())));

        LocalDate postedTime = LocalDate.now();
        File file = FileService.convertFile(dto.image());
        try {
            String url = cloudinaryService.uploadImage(file, "posts");
            Post post = new Post(
                    dto.text(), url, postedTime, user, trip
            );
            postDao.insertPost(post);
        }
        catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }

    public void deletePost(Integer id) {
        if (postDao.existsPostById(id)) {
            postDao.deletePostById(id);
        }
        else {
            throw new ResourceNotFoundException("post with id [%s] not found".formatted(id));}
    }

    public void deletePostsByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        postDao.deletePostsByUserId(userId);
    }

    public void updatePostInformation(Integer id, PostUpdateInformationDto dto) {
        Post existingPost = getPost(id);

        boolean changes = false;

        if (dto.text() != null && !dto.text().equals(existingPost.getText())) {
            existingPost.setText(dto.text());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        postDao.updatePost(existingPost);
    }

    public void updatePostPicture(Integer id, MultipartFile image) {
        Post post = getPost(id);
        try {
            File file = FileService.convertFile(image);
            String newUrl = cloudinaryService.uploadImage(file, "posts");
            if (cloudinaryService.deleteImageByUrl(post.getImage())) {
                post.setImage(newUrl);
                postDao.updatePost(post);
            }
            else {
                throw new IOException();
            }
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }
    }
}
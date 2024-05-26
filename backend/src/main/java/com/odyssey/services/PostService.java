package com.odyssey.services;

import com.odyssey.services.cloudinary.CloudinaryService;
import com.odyssey.daos.PostDao;
import com.odyssey.dtos.PostDto;
import com.odyssey.dtos.PostRegistrationDto;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.exception.UnprocessableEntityException;
import com.odyssey.services.file.FileService;
import com.odyssey.models.Post;
import com.odyssey.models.Trip;
import com.odyssey.daos.TripDao;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.dtos.PostUpdateDto;
import com.odyssey.services.utils.PostDtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostDao postDao;
    private final UserDao userDao;
    private final TripDao tripDao;
    private final CloudinaryService cloudinaryService;
    private final PostDtoMapper postDtoMapper;

    public PostService(
            @Qualifier("postJPAService") PostDao postDao,
            @Qualifier("userJPAService") UserDao userDao,
            @Qualifier("tripJPAService") TripDao tripDao,
            CloudinaryService cloudinaryService,
            PostDtoMapper postDtoMapper
    ) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.tripDao = tripDao;
        this.cloudinaryService = cloudinaryService;
        this.postDtoMapper = postDtoMapper;
    }

    public List<PostDto> getAllPosts() {
        return postDao.selectAllPosts()
                .stream().map(postDtoMapper).collect(Collectors.toList());
    }

    public List<PostDto> getPostsByUserId(Integer userId) {
        if (!userDao.existsUserById(userId)){
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return postDao.selectPostsByUserId(userId)
                .stream().map(postDtoMapper).collect(Collectors.toList());
    }

    private Post getPostById(Integer id) {
        return postDao.selectPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post with id [%s] not found".formatted(id)));
    }

    public PostDto getPost(Integer id) {
        return postDao.selectPostById(id)
                .map(postDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("post with id [%s] not found".formatted(id)));
    }

    public void addPost(PostRegistrationDto dto) {
        User user = userDao.selectUserById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("user with id [%s] not found".formatted(dto.userId())));

        Trip trip = tripDao.selectTripById(dto.tripId())
                .orElseThrow(() -> new ResourceNotFoundException("trip with id [%s] not found".formatted(dto.tripId())));

        if (postDao.existsPostByUserIdAndTripId(dto.userId(), dto.tripId())) {
            throw new DuplicateResourceException("a trip can only have one post");
        }

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

    public void updatePost(Integer id, PostUpdateDto dto) {
        Post existingPost = getPostById(id);

        boolean changes = false;

        if (dto.text() != null && !dto.text().equals(existingPost.getText())) {
            existingPost.setText(dto.text());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes");
        }

        try {
            File file = FileService.convertFile(dto.file());
            String newUrl = cloudinaryService.uploadImage(file, "posts");
            cloudinaryService.deleteImageByUrl(existingPost.getImage());
                existingPost.setImage(newUrl);
        } catch (IOException e) {
            throw new UnprocessableEntityException("image could not be processed");
        }

        postDao.updatePost(existingPost);
    }
}

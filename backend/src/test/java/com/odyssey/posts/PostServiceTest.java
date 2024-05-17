package com.odyssey.posts;

import com.odyssey.cloudinaryService.CloudinaryService;
import com.odyssey.locations.Location;
import com.odyssey.plans.Plan;
import com.odyssey.plans.PlanRegistrationRequest;
import com.odyssey.plans.PlanService;
import com.odyssey.plans.PlanUpdateRequest;
import com.odyssey.trips.Trip;
import com.odyssey.trips.TripDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostDao postDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TripDao tripDao;
    @Mock
    private CloudinaryService cloudinaryService;

    private PostService underTest;

    @BeforeEach
    void setUp() {
        underTest = new PostService(postDao, userDao, tripDao, cloudinaryService);
    }

    @Test
    void getAllPosts() {
        // When
        underTest.getAllPosts();

        // Then
        verify(postDao).selectAllPosts();
    }

    @Test
    void getPost() {
        // Given
        int id = 1;
        Post post = new Post(
                id,
                "", "",
                LocalDate.now(),
                new User(),
                new Trip()
        );

        when(postDao.selectPostById(id)).thenReturn(Optional.of(post));

        // When
        Post actual = underTest.getPost(id);

        // Then
        assertThat(actual).isEqualTo(post);
    }

    @Test
    void willThrowWhenGetPlanReturnEmptyOptional() {
        // Given
        int id = 1;
        when(postDao.selectPostById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getPost(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("post with id [%s] not found".formatted(id));
    }

    @Test
    void getPostsByUserId() {
        // Given
        List<Post> posts = new ArrayList<Post>() {};
        posts.add(new Post());
        posts.add(new Post());
        when(postDao.selectPostsByUserId(1)).thenReturn(posts);

        // When
        List<Post> actual = postDao.selectPostsByUserId(1);

        // Then
        assertThat(actual).isEqualTo(posts);
    }

    @Test
    void willThrowWhenGetPostsByUserIdUserNotExist() {
        // Given
        int id = 1;
        when(userDao.existsUserById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.getPostsByUserId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));

        // Then
        verify(postDao, never()).selectPostsByUserId(any());
    }

    @Test
    void addPost() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int tripId = 1;
        Trip trip = new Trip();
        trip.setId(tripId);
        LocalDate date = LocalDate.now();

        PostRegistrationRequest request = new PostRegistrationRequest(
                "", "", user.getId(), trip.getId()
        );
        when(postDao.existsPostByUserIdAndTripId(userId, tripId)).thenReturn(false);
        when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        when(tripDao.selectTripById(tripId)).thenReturn(Optional.of(trip));

        // When
        underTest.addPost(request);

        // Then
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postDao).insertPost(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost.getId()).isNull();
        assertThat(capturedPost.getUser().getId()).isEqualTo(request.userId());
        assertThat(capturedPost.getTrip().getId()).isEqualTo(request.tripId());
        assertThat(capturedPost.getText()).isEqualTo(request.text());
        assertThat(capturedPost.getPostedTime()).isEqualTo(date);
    }

    @Test
    void willThrowAddPostUserNotExist() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int tripId = 1;
        Trip trip = new Trip();
        trip.setId(tripId);
        LocalDate date = LocalDate.now();

        PostRegistrationRequest request = new PostRegistrationRequest(
                "", "", user.getId(), trip.getId()
        );
        lenient().when(postDao.existsPostByUserIdAndTripId(userId, tripId)).thenReturn(false);
        when(userDao.selectUserById(userId)).thenReturn(Optional.empty());
        lenient().when(tripDao.selectTripById(tripId)).thenReturn(Optional.of(trip));

        // When
        assertThatThrownBy(() -> underTest.addPost(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(request.userId()));

        // Then
        verify(postDao, never()).insertPost(any());
    }

    @Test
    void willThrowAddPostTripNotExist() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int tripId = 1;
        Trip trip = new Trip();
        trip.setId(tripId);
        LocalDate date = LocalDate.now();

        PostRegistrationRequest request = new PostRegistrationRequest(
                "", "", user.getId(), trip.getId()
        );
        lenient().when(postDao.existsPostByUserIdAndTripId(userId, tripId)).thenReturn(false);
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        when(tripDao.selectTripById(tripId)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> underTest.addPost(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("trip with id [%s] not found".formatted(request.userId()));

        // Then
        verify(postDao, never()).insertPost(any());
    }

    @Test
    void willThrowAddPostAlreadyExists() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int tripId = 1;
        Trip trip = new Trip();
        trip.setId(tripId);
        LocalDate date = LocalDate.now();

        PostRegistrationRequest request = new PostRegistrationRequest(
                "", "", user.getId(), trip.getId()
        );
        when(postDao.existsPostByUserIdAndTripId(userId, tripId)).thenReturn(true);
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        lenient().when(tripDao.selectTripById(tripId)).thenReturn(Optional.of(trip));

        // When
        assertThatThrownBy(() -> underTest.addPost(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("a trip can only have one post");

        // Then
        verify(postDao, never()).insertPost(any());
    }

    @Test
    void deletePostById() {
        // Given
        int id = 10;
        when(postDao.existsPostById(id)).thenReturn(true);

        // When
        underTest.deletePost(id);

        // Then
        verify(postDao).deletePostById(id);
    }

    @Test
    void willThrowDeletePostNotExists() {
        // Given
        int id = 10;
        when(postDao.existsPostById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deletePost(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("post with id [%s] not found".formatted(id));

        // Then
        verify(postDao, never()).deletePostById(any());
    }

    @Test
    void deletePostsByUserId() {
        // Given
        int id = 10;
        when(userDao.existsUserById(id)).thenReturn(true);

        // When
        underTest.deletePostsByUserId(id);

        // Then
        verify(postDao).deletePostsByUserId(id);
    }

    @Test
    void willThrowDeletePostsByUserIdUserNotExists() {
        // Given
        int id = 10;
        when(userDao.existsUserById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deletePostsByUserId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));

        // Then
        verify(postDao, never()).deletePostsByUserId(any());
    }

    @Test
    void updatePost() {
        // Given
        int id = 1;
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int tripId = 1;
        Trip trip = new Trip();
        trip.setId(tripId);
        LocalDate date = LocalDate.now();

        Post post = new Post(
                id, "", "", LocalDate.now(), user, trip
        );

        when(postDao.selectPostById(id)).thenReturn(Optional.of(post));

        String newText = "new text";
        String newImage = "a";
        PostUpdateRequest request = new PostUpdateRequest(
                newText, newImage
        );

        lenient().when(postDao.selectPostById(id)).thenReturn(Optional.of(post));

        // When
        underTest.updatePost(id, request);

        // Then
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postDao).updatePost(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost.getId()).isEqualTo(1);
        assertThat(capturedPost.getUser().getId()).isEqualTo(userId);
        assertThat(capturedPost.getTrip().getId()).isEqualTo(tripId);
        assertThat(capturedPost.getText()).isEqualTo(request.text());
        assertThat(capturedPost.getImage()).isEqualTo(request.image());
        assertThat(capturedPost.getPostedTime()).isEqualTo(date);

    }

}
package com.odyssey.follow;

import com.odyssey.daos.FollowDao;
import com.odyssey.dtos.FollowDeleteRequest;
import com.odyssey.dtos.FollowDto;
import com.odyssey.dtos.FollowRegistrationRequest;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Follow;
import com.odyssey.models.User;
import com.odyssey.models.Role;
import com.odyssey.daos.UserDao;
import com.odyssey.services.FollowService;
import com.odyssey.services.utils.FollowDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowDao followDao;
    @Mock
    private UserDao userDao;
    private FollowService underTest;

    private FollowDtoMapper followDtoMapper = new FollowDtoMapper();

    @BeforeEach
    void setUp() {
        underTest = new FollowService(followDao, userDao, followDtoMapper);
    }

    @Test
    void getAllFollowers() {
        // When
        underTest.getAllFollowers();

        // Then
        verify(followDao).selectAllFollowers();
    }

    @Test
    void getAllFollowingOfUserById() {
        // Given
        int id = 1;

        // When
        underTest.getAllFollowingOfUserById(id);

        // Then
        verify(followDao).selectAllFollowingOfUserById(id);
    }

    @Test
    void getAllFollowersOfUserById() {
        // Given
        int id = 1;

        // When
        underTest.getAllFollowersOfUserById(id);

        // Then
        verify(followDao).selectAllFollowersOfUserById(id);
    }

    @Test
    void getFollowById() {
        // Given
        Integer id = 1;
        Follow follow = new Follow(
                id,
                new User(1, "", "", "", "", "", new Role(1, "USER")),
                new User(2, "", "", "", "", "", new Role(1, "USER"))
        );
        FollowDto followDto = followDtoMapper.apply(follow);
        when(followDao.selectById(id)).thenReturn(Optional.of(follow));

        // When
        FollowDto actual = underTest.getFollowById(id);

        // Then
        assertThat(actual).isEqualTo(followDto);
    }

    @Test
    void willThrowWhenGetFollowReturnEmptyOptional() {
        // Given
        int id = 1;
        when(followDao.selectById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getFollowById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("follow record with id [%s] not found".formatted(id));
    }

    @Test
    void addFollower() {
        // Given
        int id = 1;
        int followerId = 1;
        int followingId = 2;
        User follower = new User(
                followerId,
                "Filan Fisteku",
                "filani",
                "filani@gmail.com",
                "passi",
                "avatar",
                new Role(1, "user")
        );

        User following = new User(
                followingId,
                "Filan Fisteku2",
                "filani2",
                "filani2@gmail.com",
                "passi2",
                "avatar2",
                new Role(1, "user")
        );

        when(followDao.existsFollowByFollowerIdAndFollowingId(1, 2)).thenReturn(false);

        FollowRegistrationRequest request = new FollowRegistrationRequest(followerId, followingId);

        when(userDao.selectUserById(followerId)).thenReturn(Optional.of(follower));
        when(userDao.selectUserById(followingId)).thenReturn(Optional.of(following));

        // When
        underTest.addFollower(request);

        // Then
        ArgumentCaptor<Follow> followArgumentCaptor = ArgumentCaptor.forClass(Follow.class);
        verify(followDao).insertFollow(followArgumentCaptor.capture());

        Follow capturedFollow = followArgumentCaptor.getValue();

        assertThat(capturedFollow.getId()).isNull();
        assertThat(capturedFollow.getFollower().getId()).isEqualTo(followerId);
        assertThat(capturedFollow.getFollowing().getId()).isEqualTo(followingId);
    }

    @Test
    void throwsAddFollowerAlreadyExists() {
        // Given
        int id = 1;
        int followerId = 1;
        int followingId = 2;
        User follower = new User(
                followerId,
                "Filan Fisteku",
                "filani",
                "filani@gmail.com",
                "passi",
                "avatar",
                new Role(1, "user")
        );

        User following = new User(
                followingId,
                "Filan Fisteku2",
                "filani2",
                "filani2@gmail.com",
                "passi2",
                "avatar2",
                new Role(1, "user")
        );

        when(followDao.existsFollowByFollowerIdAndFollowingId(1, 2)).thenReturn(true);

        FollowRegistrationRequest request = new FollowRegistrationRequest(followerId, followingId);

        when(userDao.selectUserById(followerId)).thenReturn(Optional.of(follower));
        when(userDao.selectUserById(followingId)).thenReturn(Optional.of(following));

        // When
        assertThatThrownBy(() -> underTest.addFollower(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("record already exists");

        // Then
        verify(followDao, never()).insertFollow(any());
    }

    @Test
    void throwsAddFollowerUserFollowAdmin() {
        // Given
        int id = 1;
        int followerId = 1;
        int followingId = 2;
        User follower = new User(
                followerId,
                "Filan Fisteku",
                "filani",
                "filani@gmail.com",
                "passi",
                "avatar",
                new Role(1, "user")
        );

        User following = new User(
                followingId,
                "Filan Fisteku2",
                "filani2",
                "filani2@gmail.com",
                "passi2",
                "avatar2",
                new Role(2, "admin")
        );

        when(followDao.existsFollowByFollowerIdAndFollowingId(followerId, followingId)).thenReturn(false);

        FollowRegistrationRequest request = new FollowRegistrationRequest(followerId, followingId);

        when(userDao.selectUserById(followerId)).thenReturn(Optional.of(follower));
        when(userDao.selectUserById(followingId)).thenReturn(Optional.of(following));

        // When
        assertThatThrownBy(() -> underTest.addFollower(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("a user can only follow another user");

        // Then
        verify(followDao, never()).insertFollow(any());
    }

    @Test
    void throwsAddFollowTriesToFollowSelf() {
        // Given
        int id = 1;
        int followerId = 1;
        User follower = new User(
                followerId,
                "Filan Fisteku",
                "filani",
                "filani@gmail.com",
                "passi",
                "avatar",
                new Role(1, "user")
        );

        when(followDao.existsFollowByFollowerIdAndFollowingId(followerId, followerId)).thenReturn(false);

        FollowRegistrationRequest request = new FollowRegistrationRequest(followerId, followerId);

        when(userDao.selectUserById(followerId)).thenReturn(Optional.of(follower));
        when(userDao.selectUserById(followerId)).thenReturn(Optional.of(follower));

        // When
        assertThatThrownBy(() -> underTest.addFollower(request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("a user can only follow another user");

        // Then
        verify(followDao, never()).insertFollow(any());
    }

    @Test
    void deleteFollow() {
        // Given
        int id = 10;
        when(followDao.existsFollowById(id)).thenReturn(true);

        // When
        underTest.deleteFollow(id);

        // Then
        verify(followDao).deleteFollowById(id);
    }

    @Test
    void throwsWhenDeleteFollowFollowNotFound() {
        // Given
        int id = 10;
        when(followDao.existsFollowById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteFollow(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("record with id [%s] not found".formatted(id));

        // Then
        verify(followDao, never()).deleteFollowById(any());
    }

    @Test
    void deleteFollowByFollowerIdAndFollowingId() {
        // Given
        int followerId = 1;
        int followingId = 2;
        User follower = new User();
        follower.setId(followerId);

        User following = new User();
        following.setId(followingId);

        Follow follow = new Follow(1, follower, following);
        when(followDao.selectByFollowerIdAndFollowingId(followerId, followingId)).thenReturn(Optional.of(follow));

        // When
        FollowDeleteRequest request = new FollowDeleteRequest(followerId, followingId);
        underTest.deleteFollowByFollowerIdAndFollowingId(request);

        // Then
        verify(followDao).deleteFollowById(follow.getId());
    }

    @Test
    void throwsWhenDeleteFollowByFollowerIdAndFollowingIdNotFound() {
        // Given
        int followerId = 1;
        int followingId = 2;
        User follower = new User();
        follower.setId(followerId);

        User following = new User();
        following.setId(followingId);

        Follow follow = new Follow(1, follower, following);
        when(followDao.selectByFollowerIdAndFollowingId(followerId, followingId)).thenReturn(Optional.empty());
        FollowDeleteRequest request = new FollowDeleteRequest(followerId, followingId);

        // When
        assertThatThrownBy(() -> underTest.deleteFollowByFollowerIdAndFollowingId(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] does not follow user with id [%s]".formatted(followerId, followingId));

        // Then
        verify(followDao, never()).deleteFollowById(follow.getId());
    }
}
package com.odyssey.plans;

import com.odyssey.daos.PlanDao;
import com.odyssey.dtos.PlanDto;
import com.odyssey.dtos.PlanRegistrationRequest;
import com.odyssey.dtos.PlanUpdateRequest;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Plan;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.services.PlanService;
import com.odyssey.services.utils.PlanDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.ResourceNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanDao planDao;
    @Mock
    private LocationDao locationDao;
    @Mock
    private UserDao userDao;

    private PlanService underTest;
    private final PlanDtoMapper planDtoMapper = new PlanDtoMapper();

    @BeforeEach
    void setUp() {
        underTest = new PlanService(planDao, userDao, locationDao, planDtoMapper);
    }

    @Test
    void getAllPlans() {
        // When
        underTest.getAllPlans();

        // Then
        verify(planDao).selectAllPlans();
    }

    @Test
    void getPlan() {
        // Given
        int id = 1;
        Plan plan = new Plan(
                id,
                new User(),
                new Location(),
                LocalDate.now()
        );
        PlanDto planDto = planDtoMapper.apply(plan);

        when(planDao.selectPlanById(id)).thenReturn(Optional.of(plan));

        // When
        PlanDto actual = underTest.getPlan(id);

        // Then
        assertThat(actual).isEqualTo(planDto);
    }

    @Test
    void willThrowWhenGetPlanReturnEmptyOptional() {
        // Given
        int id = 1;
        when(planDao.selectPlanById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getPlan(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("plan with id [%s] not found".formatted(id));
    }

    @Test
    void getPlansByUserId() {
        // Given
        List<Plan> plans = new ArrayList<Plan>() {};
        plans.add(new Plan());
        plans.add(new Plan());
        when(planDao.selectPlanByUserId(1)).thenReturn(plans);

        // When
        List<Plan> actual = planDao.selectPlanByUserId(1);

        // Then
        assertThat(actual).isEqualTo(plans);
    }

    @Test
    void willThrowWhenGetPlanByUserIdUserNotExist() {
        // Given
        int id = 1;
        when(userDao.existsUserById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.getPlansByUserId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));

        // Then
        verify(planDao, never()).selectPlanByUserId(any());
    }

    @Test
    void addPlan() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                userId, locationId, LocalDate.now()
        );
        when(planDao.existsPlanByUserIdAndLocationId(userId, locationId)).thenReturn(false);
        when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));

        // When
        underTest.addPlan(request);

        // Then
        ArgumentCaptor<Plan> activityArgumentCaptor = ArgumentCaptor.forClass(Plan.class);
        verify(planDao).insertPlan(activityArgumentCaptor.capture());

        Plan capturedPlan = activityArgumentCaptor.getValue();

        assertThat(capturedPlan.getId()).isNull();
        assertThat(capturedPlan.getUser().getId()).isEqualTo(request.userId());
        assertThat(capturedPlan.getLocation().getId()).isEqualTo(request.locationId());
        assertThat(capturedPlan.getVisitDate()).isEqualTo(request.date());
    }

    @Test
    void willThrowAddPlanUserNotExist() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                userId, locationId, LocalDate.now()
        );
        lenient().when(planDao.existsPlanByUserIdAndLocationId(userId, locationId)).thenReturn(false);
        when(userDao.selectUserById(userId)).thenReturn(Optional.empty());
        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));

        // When
        assertThatThrownBy(() -> underTest.addPlan(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(request.userId()));

        // Then
        verify(planDao, never()).insertPlan(any());
    }

    @Test
    void willThrowAddPlanLocationNotExist() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                userId, locationId, LocalDate.now()
        );
        lenient().when(planDao.existsPlanByUserIdAndLocationId(userId, locationId)).thenReturn(false);
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        when(locationDao.selectLocationById(locationId)).thenReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> underTest.addPlan(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("location with id [%s] not found".formatted(request.userId()));

        // Then
        verify(planDao, never()).insertPlan(any());
    }

    @Test
    void willThrowAddPlanAlreadyExists() {
        // Given
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);

        PlanRegistrationRequest request = new PlanRegistrationRequest(
                userId, locationId, LocalDate.now()
        );
        when(planDao.existsPlanByUserIdAndLocationId(userId, locationId)).thenReturn(true);
        lenient().when(userDao.selectUserById(userId)).thenReturn(Optional.of(user));
        lenient().when(locationDao.selectLocationById(locationId)).thenReturn(Optional.of(location));

        // When
        assertThatThrownBy(() -> underTest.addPlan(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("plan already exists");

        // Then
        verify(planDao, never()).insertPlan(any());
    }

    @Test
    void deletePlanById() {
        // Given
        int id = 10;
        when(planDao.existsPlanById(id)).thenReturn(true);

        // When
        underTest.deletePlan(id);

        // Then
        verify(planDao).deletePlanById(id);
    }

    @Test
    void willThrowDeletePlanNotExists() {
        // Given
        int id = 10;
        when(planDao.existsPlanById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deletePlan(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("plan with id [%s] not found".formatted(id));

        // Then
        verify(planDao, never()).deletePlanById(any());
    }

    @Test
    void deletePlansByUserId() {
        // Given
        int id = 10;
        when(userDao.existsUserById(id)).thenReturn(true);

        // When
        underTest.deletePlansByUserId(id);

        // Then
        verify(planDao).deletePlanByUserId(id);
    }

    @Test
    void willThrowDeletePlansByUserIdUserNotExists() {
        // Given
        int id = 10;
        when(userDao.existsUserById(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deletePlansByUserId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user with id [%s] not found".formatted(id));

        // Then
        verify(planDao, never()).deletePlanByUserId(any());
    }

    @Test
    void updatePlan() throws ParseException {
        // Given
        int id = 1;
        int userId = 1;
        User user = new User();
        user.setId(userId);
        int locationId = 1;
        Location location = new Location();
        location.setId(locationId);
        LocalDate date = LocalDate.now();
        Plan plan = new Plan(id, user, location, date);

        when(planDao.selectPlanById(id)).thenReturn(Optional.of(plan));

        int newLocationId = 1;
        Location newLocation = new Location();
        newLocation.setId(newLocationId);
        LocalDate newDate = date.plus(90, ChronoUnit.DAYS);
        PlanUpdateRequest request = new PlanUpdateRequest(
                newLocationId, newDate
        );

        when(locationDao.selectLocationById(newLocationId)).thenReturn(Optional.of(location));
        lenient().when(planDao.existsPlanByUserIdAndLocationId(userId, newLocationId)).thenReturn(false);

        // When
        underTest.updatePlan(id, request);

        // Then
        ArgumentCaptor<Plan> activityArgumentCaptor = ArgumentCaptor.forClass(Plan.class);
        verify(planDao).updatePlan(activityArgumentCaptor.capture());

        Plan capturedPlan = activityArgumentCaptor.getValue();

        assertThat(capturedPlan.getId()).isEqualTo(id);
        assertThat(capturedPlan.getLocation().getId()).isEqualTo(request.locationId());
        assertThat(capturedPlan.getVisitDate()).isEqualTo(request.date());

    }

}
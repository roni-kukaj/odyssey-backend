package com.odyssey.recommendations;

import com.odyssey.activities.Activity;
import com.odyssey.user.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class RecommendationJPADataAccessServiceTest {

    private RecommendationJPADataAccessService recommendationJPADataAccessService;
    private AutoCloseable autoCloseable;

    @Mock
    private RecommendationRepository recommendationRepository;


    @BeforeEach
    void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        recommendationJPADataAccessService = new RecommendationJPADataAccessService(recommendationRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();

    }

    @Test
    void selectAllRecommendations() {
        recommendationJPADataAccessService.selectAllRecommendations();
        verify(recommendationRepository).findAll();
    }

    @Test
    void selectRecommendationById() {
        int id = 1;
        recommendationJPADataAccessService.selectRecommendationById(id);
        verify(recommendationRepository).findById(id);

    }

    @Test
    void selectRecommendationByUserId() {
        int id = 1;
        recommendationJPADataAccessService.selectRecommendationByUserId(id);
        verify(recommendationRepository).findRecommendationByUserId(id);
    }

    @Test
    void selectRecommendationByActivityId(){
        int id = 1;
        recommendationJPADataAccessService.selectRecommendationByActivityId(id);
        verify(recommendationRepository).findRecommendationByActivityId(id);
    }

    @Test
    void insertRecommendation() {
        Recommendation recommendation = new Recommendation("A must - visit place", new User(),new Activity());
        recommendationJPADataAccessService.insertRecommendation(recommendation);
        verify(recommendationRepository).save(recommendation);
    }

    @Test
    void existsRecommendationById() {
        int id=1;
        recommendationJPADataAccessService.existsRecommendationById(id);
        verify(recommendationRepository).existsRecommendationById(id);
    }

    @Test
    void existsRecommendationByUserAndActivityId() {
        int userid = 1;
        int activityid = 1;
        recommendationJPADataAccessService.existsRecommendationByUserIdAndActivityId(userid,activityid);
        verify(recommendationRepository).existsRecommendationByUserIdAndActivityId(userid,activityid);
    }


    @Test
    void updateRecommendation() {
        Recommendation recommendation = new Recommendation("Must visit",new User(),new Activity());
        recommendationJPADataAccessService.upateRecommendation(recommendation);
        verify(recommendationRepository).save(recommendation);
    }

    @Test
    void deleteRecommendationById() {
        int id = 2;
        recommendationJPADataAccessService.deleteRecommendationById(id);
        verify(recommendationRepository).deleteById(id);
    }
}

package com.odyssey.services;



import com.odyssey.daos.ReviewDao;
import com.odyssey.dtos.ReviewDto;
import com.odyssey.dtos.ReviewRegistrationRequest;
import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.models.Location;
import com.odyssey.daos.LocationDao;
import com.odyssey.models.Review;
import com.odyssey.models.User;
import com.odyssey.daos.UserDao;
import com.odyssey.dtos.ReviewUpdateRequest;
import com.odyssey.services.utils.ReviewDtoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final LocationDao locationDao;
    private final ReviewDtoMapper reviewDtoMapper;

    public ReviewService(ReviewDao reviewDao, UserDao userDao, LocationDao locationDao, ReviewDtoMapper reviewDtoMapper) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.locationDao = locationDao;
        this.reviewDtoMapper = reviewDtoMapper;
    }

    private Review getReviewById(Integer id) {
        return reviewDao.selectReviewById(id)
                .orElseThrow(()-> new ResourceNotFoundException("review with id [%s] not found".formatted(id)));
    }

    public List<ReviewDto>getAllReviews(){
        return reviewDao.selectAllReviews()
                .stream().map(reviewDtoMapper).collect(Collectors.toList());
    }

    public ReviewDto getReview(Integer id){
        return reviewDao.selectReviewById(id)
                .map(reviewDtoMapper)
                .orElseThrow(()-> new ResourceNotFoundException("review with id [%s] not found".formatted(id)));
    }

    public List<ReviewDto> getReviewByUserId(Integer userId){
        if (!userDao.existsUserById(userId)) {
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));
        }
        return reviewDao.selectReviewByUserId(userId)
                .stream().map(reviewDtoMapper).collect(Collectors.toList());
    }

    public List<ReviewDto>getReviewByLocationId(Integer locationId) {
        if (!locationDao.existsLocationById(locationId)) {
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return reviewDao.selectReviewByLocationId(locationId)
                .stream().map(reviewDtoMapper).collect(Collectors.toList());
    }

    public void addReview(ReviewRegistrationRequest reviewRegisterRequest){
       if(reviewDao.existsReviewByUserAndLocationId(reviewRegisterRequest.userId(),reviewRegisterRequest.locationId())){
           throw new DuplicateResourceException("review already exists");
       }
       Location location = locationDao.selectLocationById(reviewRegisterRequest.locationId())
                .orElseThrow(()->new ResourceNotFoundException("location with id [%s] not found".formatted(reviewRegisterRequest.locationId())));

       User user = userDao.selectUserById(reviewRegisterRequest.userId())
               .orElseThrow(()-> new ResourceNotFoundException("user with id [%s] not found".formatted(reviewRegisterRequest.userId())));

       Review review = new Review(reviewRegisterRequest.description(), reviewRegisterRequest.rating(), user, location);

       reviewDao.insertReview(review);
    }

    public void deleteReview(Integer id){
        if(reviewDao.existsReviewById(id)){
            reviewDao.deleteReviewById(id);
        }
        else {
            throw new ResourceNotFoundException("review with id [%s] not found".formatted(id));
        }
    }

    public void updateReview(Integer id, ReviewUpdateRequest reviewUpdateRequest){
        Review existingReview = getReviewById(id);

        boolean changes = false;

        if(reviewUpdateRequest.description() != null && !reviewUpdateRequest.description().equals(existingReview.getDescription())){
            existingReview.setDescription(reviewUpdateRequest.description());
            changes = true;
        }

        if(reviewUpdateRequest.rating() != null && !reviewUpdateRequest.rating().equals(existingReview.getRating())){
            existingReview.setRating(reviewUpdateRequest.rating());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no changes were found");
        }

        reviewDao.updateReview(existingReview);
    }
}

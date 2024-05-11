package com.odyssey.reviews;



import com.odyssey.exception.DuplicateResourceException;
import com.odyssey.exception.RequestValidationException;
import com.odyssey.exception.ResourceNotFoundException;
import com.odyssey.locations.Location;
import com.odyssey.locations.LocationDao;
import com.odyssey.user.User;
import com.odyssey.user.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewDao reviewDao;
    private final UserDao userDao;
    private final LocationDao locationDao;

    public ReviewService(ReviewDao reviewDao, UserDao userDao, LocationDao locationDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.locationDao = locationDao;
    }

    public List<Review>getAllReviews(){
        return reviewDao.selectAllReviews();

    }

    public Review getReview(Integer id){
        return reviewDao.selectReviewById(id).
                orElseThrow(()-> new ResourceNotFoundException("review with id [%s] not found".formatted(id)));

    }

    public List<Review> getReviewByUserId(Integer userId){
        if(!userDao.existsUserById(userId)){
            throw new ResourceNotFoundException("user with id [%s] not found".formatted(userId));

        }
        return reviewDao.selectReviewByUserId(userId);
    }

    public List<Review>getReviewByLocationId(Integer locationId){
        if(!locationDao.existsLocationById(locationId)){
            throw new ResourceNotFoundException("location with id [%s] not found".formatted(locationId));
        }
        return reviewDao.selectReviewByLocationId(locationId);
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


    public boolean deleteReview(Integer id){
        if(reviewDao.existsReviewById(id)){
            reviewDao.deleteReviewById(id);
        }
        else {
            throw new ResourceNotFoundException("review with id [%s] not found".formatted(id));
        }
        return false;
    }


    public boolean updateReview(Integer id, ReviewUpdateRequest reviewUpdateRequest){
        Review existingReview = getReview(id);

        if(reviewDao.existsReviewByUserAndLocationId(reviewUpdateRequest.userId(),reviewUpdateRequest.locationId())){
            throw new DuplicateResourceException("review already exists");
        }

        User user = userDao.selectUserById(reviewUpdateRequest.userId()).orElseThrow(
                ()-> new ResourceNotFoundException("user with id [%s] not found".formatted(reviewUpdateRequest.userId()))
        );

       Location location = locationDao.selectLocationById(reviewUpdateRequest.locationId()).orElseThrow(
               ()-> new ResourceNotFoundException("location with id [%s] not found".formatted(reviewUpdateRequest.locationId()))
       );

        boolean changes = false;

        if(reviewUpdateRequest.description() != null && !reviewUpdateRequest.description().equals(existingReview.getDescription())){
            existingReview.setDescription(reviewUpdateRequest.description());
            changes = true;
        }
        if(reviewUpdateRequest.rating() != null && !reviewUpdateRequest.rating().equals(existingReview.getRating())){
            existingReview.setRating(reviewUpdateRequest.rating());
            changes = true;
        }

        if(reviewUpdateRequest.userId() != null && !reviewUpdateRequest.userId().equals(existingReview.getUser().getId())){
            existingReview.setUser(user);
            changes = true;
        }

        if(reviewUpdateRequest.locationId() != null && !reviewUpdateRequest.locationId().equals(existingReview.getLocation().getId())){
            existingReview.setLocation(location);
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no changes were found");
        }

        reviewDao.updateReview(existingReview);
        return changes;

    }




}

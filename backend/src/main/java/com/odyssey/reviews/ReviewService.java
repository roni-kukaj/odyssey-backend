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
                orElseThrow(()-> new ResourceNotFoundException("Review with id [%s] not found".formatted(id)));

    }

    public List<Review> getReviewByUserId(Integer user_id){
        if(!userDao.existsUserById(user_id)){
            throw new ResourceNotFoundException("User with id [%s] not found".formatted(user_id));

        }
        return reviewDao.selectReviewByUserId(user_id);
    }

    public List<Review>getReviewByLocationId(Integer location_id){
        if(!locationDao.existsLocationById(location_id)){
            throw new ResourceNotFoundException("Location with id [%s] not found".formatted(location_id));
        }
        return reviewDao.selectReviewByLocationId(location_id);
    }

    public void addReview(ReviewRegistrationRequest reviewRegisterRequest){
       if(reviewDao.existsReviewByUserAndLocationId(reviewRegisterRequest.user_id(),reviewRegisterRequest.location_id())){
           throw new DuplicateResourceException("Review already exists");
       }
        Location location = locationDao.selectLocationById(reviewRegisterRequest.location_id())
                .orElseThrow(()->new ResourceNotFoundException("Location with id [%s] not found".formatted(reviewRegisterRequest.location_id())));

       User user = userDao.selectUserById(reviewRegisterRequest.user_id())
               .orElseThrow(()-> new ResourceNotFoundException("User with id [%s] not found".formatted(reviewRegisterRequest.user_id())));



       Review review = new Review(reviewRegisterRequest.description(),reviewRegisterRequest.rating(),user,location);

       reviewDao.insertReview(review);
    }


    public boolean deleteReview(Integer id){
        if(reviewDao.existsReviewById(id)){
            reviewDao.deleteReviewById(id);
        }
        else {
            throw new ResourceNotFoundException("Review with id [%s] not found".formatted(id));
        }
        return false;
    }


    public boolean updateReview(Integer id, ReviewUpdateRequest reviewUpdateRequest){
        Review existingReview = getReview(id);

        if(reviewDao.existsReviewByUserAndLocationId(reviewUpdateRequest.user_id(),reviewUpdateRequest.location_id())){
            throw new DuplicateResourceException("Review already exists");
        }

        User user = userDao.selectUserById(reviewUpdateRequest.user_id()).orElseThrow(
                ()-> new ResourceNotFoundException("User with id [%s] not found".formatted(reviewUpdateRequest.user_id()))
        );

       Location location = locationDao.selectLocationById(reviewUpdateRequest.location_id()).orElseThrow(
               ()-> new ResourceNotFoundException("Location with id [%s] not found".formatted(reviewUpdateRequest.location_id()))
       );

        boolean changes = false;

        if(reviewUpdateRequest.description()!=null && !reviewUpdateRequest.description().equals(existingReview.getDescription())){
            existingReview.setDescription(reviewUpdateRequest.description());
            changes = true;
        }
        if(reviewUpdateRequest.rating()!=null && !reviewUpdateRequest.rating().equals(existingReview.getRating())){
            existingReview.setRating(reviewUpdateRequest.rating());
            changes = true;
        }

        if(reviewUpdateRequest.user_id()!=null && !reviewUpdateRequest.user_id().equals(existingReview.getUser().getId())){
            existingReview.setUser(user);
            changes = true;
        }

        if(reviewUpdateRequest.location_id()!=null && !reviewUpdateRequest.location_id().equals(existingReview.getLocation().getId())){
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

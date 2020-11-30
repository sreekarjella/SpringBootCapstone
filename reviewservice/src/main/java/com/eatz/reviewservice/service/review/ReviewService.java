package com.eatz.reviewservice.service.review;

import java.util.List;

import com.eatz.reviewservice.dto.ReviewDTO;
import com.eatz.reviewservice.dto.ReviewUpdateDTO;
import com.eatz.reviewservice.model.Review;

public interface ReviewService {

	Review addReview(ReviewDTO review);

	Review updateReview(ReviewUpdateDTO review);

	List<Review> getAllReviewForRestaurant(long restaurantID);

}

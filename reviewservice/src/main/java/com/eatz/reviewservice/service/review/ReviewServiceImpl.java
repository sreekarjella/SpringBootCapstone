package com.eatz.reviewservice.service.review;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatz.reviewservice.dto.ReviewDTO;
import com.eatz.reviewservice.dto.ReviewUpdateDTO;
import com.eatz.reviewservice.exception.InvalidReviewException;
import com.eatz.reviewservice.exception.ReviewNotFoundException;
import com.eatz.reviewservice.model.Review;
import com.eatz.reviewservice.repository.ReviewRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

	@Override
	public Review addReview(ReviewDTO review) {
		validateReview(review.getReview());
		logger.debug("Validation successfull. Saving the review");
		Review toPersist = new Review();
		toPersist.setCustomerID(review.getCustomerID());
		toPersist.setRestaurantID(review.getRestaurantID());
		toPersist.setReview(review.getReview());
		return reviewRepository.save(toPersist);
	}

	private void validateReview(String review) {
		logger.debug("Validating the review.");
		if (review.isEmpty()) {
			throw new InvalidReviewException("Please provide review to be posted");
		}
	}

	@Override
	public Review updateReview(ReviewUpdateDTO review) {
		validateReview(review.getReview());
		logger.debug("Validation successfull. Updating the review");
		Review toPersist = new Review();
		toPersist.setId(review.getId());
		toPersist.setCustomerID(review.getCustomerID());
		toPersist.setRestaurantID(review.getRestaurantID());
		toPersist.setReview(review.getReview());
		return reviewRepository.save(toPersist);
	}

	@Override
	public List<Review> getAllReviewForRestaurant(long restaurantID) {
		logger.debug("Fetching reviews for a restaurant by restaurantID");
		List<Review> reviews = reviewRepository.findAllReviewsByRestaurantID(restaurantID);
		if (reviews.isEmpty()) {
			logger.debug("No reviews found for the restaurantID: " + restaurantID);
			logger.debug("Throwing Exception");
			throw new ReviewNotFoundException("No reviews for the specified restaurant");
		}
		logger.debug("Found reviews and returning the restaurant");
		return reviews;
	}

}

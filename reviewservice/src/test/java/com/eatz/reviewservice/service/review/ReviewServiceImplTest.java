package com.eatz.reviewservice.service.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.eatz.reviewservice.dto.ReviewDTO;
import com.eatz.reviewservice.dto.ReviewUpdateDTO;
import com.eatz.reviewservice.exception.InvalidReviewException;
import com.eatz.reviewservice.exception.ReviewNotFoundException;
import com.eatz.reviewservice.model.Review;
import com.eatz.reviewservice.repository.ReviewRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ReviewServiceImplTest {

	@InjectMocks
	ReviewServiceImpl service;

	@Mock
	private ReviewRepository repository;

	long id = 1L;
	long customerID = 1L;
	long restaurantID = 2L;
	String review = "review";

	@Test
	void testAddReview() {
		ReviewDTO reviewDTO = new ReviewDTO(customerID, restaurantID, review);
		Review response = new Review(id, customerID, restaurantID, review);
		when(repository.save(any(Review.class))).thenReturn(response);
		assertNotNull(service.addReview(reviewDTO));
	}

	@Test
	void testAddReview_invalidReview() {
		ReviewDTO reviewDTO = new ReviewDTO(customerID, restaurantID, "");
		assertThrows(InvalidReviewException.class, () -> {
			service.addReview(reviewDTO);
		});
	}

	@Test
	void testUpdateReview() {
		ReviewUpdateDTO updateDTO = new ReviewUpdateDTO(id, customerID, restaurantID, review);
		Review response = new Review(id, customerID, restaurantID, review);
		when(repository.save(any(Review.class))).thenReturn(response);
		assertNotNull(service.updateReview(updateDTO));
	}
	
	@Test
	void testUpdateReview_invalidReview() {
		ReviewUpdateDTO updateDTO = new ReviewUpdateDTO(id, customerID, restaurantID, "");
		assertThrows(InvalidReviewException.class, () -> {
			service.updateReview(updateDTO);
		});
	}

	@Test
	void testGetAllReviewForRestaurant() {
		List<Review> reviews = new ArrayList<>();
		reviews.add(new Review(id, customerID, restaurantID, review));
		when(repository.findAllReviewsByRestaurantID(id)).thenReturn(reviews);
		assertNotNull(service.getAllReviewForRestaurant(id));
	}

	@Test
	void testGetAllReviewForRestaurant_noReviews() {
		List<Review> reviews = new ArrayList<>();
		when(repository.findAllReviewsByRestaurantID(id)).thenReturn(reviews);
		assertThrows(ReviewNotFoundException.class, () -> {
			service.getAllReviewForRestaurant(id);
		});
	}
}

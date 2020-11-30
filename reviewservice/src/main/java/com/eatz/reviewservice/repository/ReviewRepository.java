package com.eatz.reviewservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatz.reviewservice.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findAllReviewsByRestaurantID(long restaurantID);
}

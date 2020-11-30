package com.eatz.reviewservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatz.reviewservice.dto.ReviewDTO;
import com.eatz.reviewservice.dto.ReviewUpdateDTO;
import com.eatz.reviewservice.model.Review;
import com.eatz.reviewservice.service.review.ReviewService;

@RestController
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	KafkaTemplate<String, ReviewDTO> kafkaTemplate;
	private static final String KAFKA_TOPIC = "REVIEW";

	private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

	@RequestMapping(path = "/review", method = RequestMethod.POST)
	public ResponseEntity<String> addReview(@RequestHeader String authorization, @RequestBody ReviewDTO review) {
		logger.debug("In addReview method calling Review Service");
		reviewService.addReview(review);
		logger.debug("Successfully saved the review");
		logger.debug("Sending a notification to kafka queue");
		kafkaTemplate.send(KAFKA_TOPIC, review);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully posted the review");
	}

	@RequestMapping(path = "/change-review", method = RequestMethod.PUT)
	public ResponseEntity<String> updateReview(@RequestHeader String authorization,
			@RequestBody ReviewUpdateDTO review) {
		logger.debug("In updateReview calling Review Service");
		reviewService.updateReview(review);
		logger.debug("Successfully updated review");
		return ResponseEntity.status(HttpStatus.OK).body("Successfully updated the review");
	}

	@RequestMapping(path = "/all-reviews", method = RequestMethod.GET)
	public ResponseEntity<List<Review>> getAllReviewForRestaurant(@RequestHeader String authorization,
			@RequestParam(name = "id") long restaurantID) {
		logger.debug("In getAllReviewForRestaurant calling Review Service");
		List<Review> reviews = reviewService.getAllReviewForRestaurant(restaurantID);
		logger.debug("Found reviews. Sending response");
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}
}

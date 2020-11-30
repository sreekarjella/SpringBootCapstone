package com.eatz.reviewservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.eatz.reviewservice.ReviewserviceApplication;
import com.eatz.reviewservice.dto.ReviewDTO;
import com.eatz.reviewservice.dto.ReviewUpdateDTO;
import com.eatz.reviewservice.model.Review;
import com.eatz.reviewservice.service.review.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReviewController.class)
@ContextConfiguration(classes = ReviewserviceApplication.class)
class ReviewControllerTest {

	@Autowired
	private MockMvc mockMVC;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ReviewService service;
	
	@MockBean
	private KafkaTemplate<String, ReviewDTO> kafka;

	private static final long EXPIRATIONTIME = 900000;
	String jwt = "";
	long id = 1L;
	long customerID = 1L;
	long restaurantID = 2L;
	String review = "review";

	@BeforeEach
	public void setup() {
		jwt = "Bearer " + Jwts.builder().setSubject("user").claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "secretkey")
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)).compact();
	}

	@Test
	void testAddReview() throws Exception {
		ReviewDTO reviewDTO = new ReviewDTO(customerID, restaurantID, review);
		Review response = new Review(id, customerID, restaurantID, review);
		when(service.addReview(any(ReviewDTO.class))).thenReturn(response);
		System.out.println(response.getCustomerID());
		System.out.println(response.getId());
		System.out.println(response.getRestaurantID());
		System.out.println(response.getReview());
		RequestBuilder request = MockMvcRequestBuilders.post("/review").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reviewDTO)).header(HttpHeaders.AUTHORIZATION, jwt);
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

	@Test
	void testUpdateReview() throws Exception {
		ReviewUpdateDTO updateDTO = new ReviewUpdateDTO(id, customerID, restaurantID, review);
		Review response = new Review(id, customerID, restaurantID, review);
		when(service.updateReview(any(ReviewUpdateDTO.class))).thenReturn(response);
		RequestBuilder request = MockMvcRequestBuilders.put("/change-review").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDTO)).header(HttpHeaders.AUTHORIZATION, jwt);
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

	@Test
	void testGetAllReviewForRestaurant() throws Exception {
		List<Review> reviews = new ArrayList<>();
		when(service.getAllReviewForRestaurant(anyLong())).thenReturn(reviews);
		RequestBuilder request = MockMvcRequestBuilders.get("/all-reviews").contentType(MediaType.APPLICATION_JSON)
				.param("id", String.valueOf(id)).header(HttpHeaders.AUTHORIZATION, jwt);
		mockMVC.perform(request).andExpect(status().is(200)).andReturn();
	}

}

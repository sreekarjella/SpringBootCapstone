package com.eatza.restaurantsearch.service.kafka;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.eatza.restaurantsearch.dto.CustomerResponseDTO;
import com.eatza.restaurantsearch.dto.ReviewDTO;
import com.eatza.restaurantsearch.exception.CustomerNotFoundException;
import com.eatza.restaurantsearch.utils.EMailer;

@Service
public class KafkaConsumer {

	@Autowired
	private EMailer mailer;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${customer.get.byid.url}")
	private String customerServiceEmail;

	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(topics = "REVIEW", groupId = "review_notification")
	public void reviewMailTrigger(ReviewDTO data) throws IOException {
		ReviewDTO review = data;
		System.out.println(review.getCustomerID() + "\t" + review.getRestaurantID() + "\t" + review.getReview());
		logger.debug("Fetched the review from Kafka");
		CustomerResponseDTO customer = findCustomer(review.getCustomerID());
		if (!customer.getEmail().isEmpty()) {
			logger.debug("Customer found with ID " + review.getCustomerID());
			logger.debug("Sending email to: " + customer.getEmail());
			String message = "Thank you " + customer.getFirstName() + "," + " for posting a review.\n"
					+ "You have posted " + review.getReview();
			mailer.sendEmail(customer.getEmail(), "Posted review on EATZA", message);
			logger.debug("Email sent");
		}

	}

	CustomerResponseDTO findCustomer(long customerID) {
		logger.debug("Building ther URL for calling the customer service");
		String uri = customerServiceEmail + "?id=" + customerID;
		CustomerResponseDTO customer;
		try {
			logger.debug("Calling the customer service");
			customer = restTemplate.getForObject(uri, CustomerResponseDTO.class);
		} catch (HttpClientErrorException e) {
			logger.debug("ID is invalid");
			throw new CustomerNotFoundException("Provided ID does not exist");
		}
		return customer;
	}

}

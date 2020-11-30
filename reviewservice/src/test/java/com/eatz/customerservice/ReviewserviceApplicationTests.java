package com.eatz.customerservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.eatz.reviewservice.ReviewserviceApplication;

@SpringBootTest(classes = ReviewserviceApplication.class)
class ReviewserviceApplicationTests {

	@Test
	void contextLoads() {
		ReviewserviceApplication.main(new String[] {});
	}

}

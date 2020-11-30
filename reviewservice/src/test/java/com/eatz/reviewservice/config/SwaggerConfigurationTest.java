package com.eatz.reviewservice.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.eatz.reviewservice.ReviewserviceApplication;

import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ReviewserviceApplication.class)
class SwaggerConfigurationTest {

	@Autowired
	Docket docket;

	@Autowired
	UiConfiguration uiconfiguration;

	@Test
	public void docketTest() {
		assertNull(docket);
	}

	@Test
	public void uiconfigurationTest() {
		assertNull(uiconfiguration);
	}

}

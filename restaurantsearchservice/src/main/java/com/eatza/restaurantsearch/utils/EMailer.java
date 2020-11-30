package com.eatza.restaurantsearch.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EMailer {

	@Autowired
	private JavaMailSender mailSender;
	private static final String fromAddress = "sreekarjella1@gmail.com";
	
	private static final Logger logger = LoggerFactory.getLogger(EMailer.class);
	
	public void sendEmail(String toAddress, String subject, String message) {
		logger.debug("Preparing email to send");
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(toAddress);
		email.setFrom(fromAddress);
		email.setSubject(subject);
		email.setText(message);
		mailSender.send(email);
	}
}

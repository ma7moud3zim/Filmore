package com.azim.filmore.serviceImpl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.azim.filmore.exception.EmailNotVerifiedException;
import com.azim.filmore.service.EmailService;


@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${app.frontend.url:http://localhost:4200}")
	private String frontendUrl;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	@Override
	public void sendVerificationEmail(String toEmail, String token) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(toEmail); 
			message.setSubject("Filmore - Email Verification");
			String verificationUrl = frontendUrl + "/verify-email?token=" + token;
			
			message.setText("Thank you for registering with Filmore."
					+ " \n Please click the link below to verify your email address: " + verificationUrl);
			javaMailSender.send(message);
			logger.info("Verification email sent to {}", toEmail);
			
		}catch(Exception ex) {
			logger.error("Failed to send verification email to {}:{}", toEmail, ex.getMessage(), ex);
			throw new EmailNotVerifiedException("Failed to send verification email");
		}
	}

	@Override
	public void sendPasswordResetEmail(String toEmail, String token) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo(toEmail);
			message.setSubject("Filmore - Password Reset");
			String resetUrl = frontendUrl + "/reset-password?token=" + token;
			
			message.setText("Here is your reset email, Please click the link below to reset your password: " + resetUrl);
			javaMailSender.send(message);
			logger.info("Password reset email sent to {}", toEmail);
		}catch(Exception ex) {
			logger.error("Failed to send password reset email to {}: {}" , toEmail, ex.getMessage(), ex);
			throw new RuntimeException("Failed to send password reset email");
		}
		
	}
	
}

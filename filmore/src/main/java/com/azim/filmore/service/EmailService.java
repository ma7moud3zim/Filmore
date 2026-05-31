package com.azim.filmore.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
	void sendVerificationEmail(String toEmail, String token);
	
	void sendPasswordResetEmail(String toEmail, String token);

}

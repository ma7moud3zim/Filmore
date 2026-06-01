package com.azim.filmore.service;

import org.springframework.stereotype.Service;

import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.LoginResponse;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.EmailValidationResponse;

import jakarta.validation.Valid;


@Service
public interface AuthService {
	MessageResponse signUp(@Valid UserRequest userRequest);

	LoginResponse login(String email, String password);

	EmailValidationResponse validateEmail(String token);

	MessageResponse verifyEmail( String token);

	MessageResponse resendVerification(String email);

	MessageResponse forgotPassword(String email);

	MessageResponse resetPassword(String token, String newPassword);

	MessageResponse changePassword(String email, String currentPassword, String newPassword);


}

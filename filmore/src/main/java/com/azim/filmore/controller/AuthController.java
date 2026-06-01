package com.azim.filmore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azim.filmore.dto.request.ChangePasswordRequest;
import com.azim.filmore.dto.request.EmailRequest;
import com.azim.filmore.dto.request.LoginRequest;
import com.azim.filmore.dto.request.ResetPasswordRequest;
import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.EmailValidationResponse;
import com.azim.filmore.dto.response.LoginResponse;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> signUp(@Valid @RequestBody UserRequest userRequest) {
		return ResponseEntity.ok(authService.signUp(userRequest));
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		LoginResponse response = authService.login(loginRequest.getEmail() , loginRequest.getPassword());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/validate-email")
	public ResponseEntity<EmailValidationResponse> validateEmail(String token) {
		return ResponseEntity.ok(authService.validateEmail(token));
	}
	
	@GetMapping("/verify-email")
	public ResponseEntity<MessageResponse> verifyEmail(@Valid @RequestParam String token) {
		return ResponseEntity.ok(authService.verifyEmail(token));
	}
	
	@PostMapping("/resend-verification")
	public ResponseEntity<MessageResponse> resendVerificationEmail(@Valid @RequestBody EmailRequest emailRequest) {
		return ResponseEntity.ok(authService.resendVerification(emailRequest.getEmail()));
	}
	
	
	@PostMapping("/forget-password")
	public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody EmailRequest emailRequest) { 
		return ResponseEntity.ok(authService.forgotPassword(emailRequest.getEmail()));
	} 
	
	@PostMapping("/reset-password")
	public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
		return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword()));
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<MessageResponse> changePassword(Authentication auth, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		
		String email = auth.getName();
		return ResponseEntity.ok(authService.changePassword(email,changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword()));
	}
	
	
	@GetMapping("/current-user")
	public ResponseEntity<LoginResponse> getCurrentUser(Authentication auth) {
		return ResponseEntity.ok(authService.currentUser(auth.getName()));
	}
	
	
	
	
	
}



















package com.azim.filmore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azim.filmore.dto.request.LoginRequest;
import com.azim.filmore.dto.request.UserRequest;
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
}

package com.azim.filmore.service;

import org.springframework.stereotype.Service;

import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.MessageResponse;

import jakarta.validation.Valid;


@Service
public interface AuthService {
	MessageResponse signUp(@Valid UserRequest userRequest);
}

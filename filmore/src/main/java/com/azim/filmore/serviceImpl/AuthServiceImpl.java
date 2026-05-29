package com.azim.filmore.serviceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.azim.filmore.dao.UserRepository;
import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.entity.User;
import com.azim.filmore.enums.Role;
import com.azim.filmore.exception.EmailAlreadyExistsException;
import com.azim.filmore.security.JwtUtil;
import com.azim.filmore.service.AuthService;
import com.azim.filmore.service.EmailService;
import com.azim.filmore.util.ServiceUtils;

import jakarta.validation.Valid;



@Service
public class AuthServiceImpl implements AuthService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ServiceUtils serviceUtils;
	
	@Override
	public MessageResponse signUp(@Valid UserRequest userRequest) {
		if(userRepository.existsByEmail(userRequest.getEmail())) {
			throw new EmailAlreadyExistsException("Email already exists");
		}
		
		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setFullName(userRequest.getFullName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(Role.USER);
		user.setActive(true);
		user.setEmailVarified(false);
		String verificationToken = UUID.randomUUID().toString();
		user.setVarificationToken(verificationToken);
		user.setVarificationTokenExpiry(Instant.now().plusSeconds(86400));
		userRepository.save(user);
		
		emailService.sendVerificationEmail(userRequest.getEmail(), verificationToken);
		
		return new MessageResponse("Registeration successfull! Please check your email to verify your account.");
	}

}

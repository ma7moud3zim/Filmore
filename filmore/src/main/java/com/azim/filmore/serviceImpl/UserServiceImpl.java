package com.azim.filmore.serviceImpl;

import java.time.Instant;
import java.util.Arrays;
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
import com.azim.filmore.exception.InvalidRoleException;
import com.azim.filmore.service.EmailService;
import com.azim.filmore.service.UserService;
import com.azim.filmore.util.ServiceUtils;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ServiceUtils serviceUtils;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public MessageResponse createUser(UserRequest userRequest) {
		if(userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
			throw new EmailAlreadyExistsException("Email already exists");
		}
		validateRole(userRequest.getRole());
		
		User user = new User();
		user.setEmail(userRequest.getEmail());
		user.setFullName(userRequest.getFullName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(Role.valueOf(userRequest.getRole()));
		user.setActive(true);
		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken);
		user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
		userRepository.save(user);
		
		emailService.sendVerificationEmail(userRequest.getEmail(), verificationToken);
		
		return new MessageResponse("Registeration successfull! Please check your email to verify your account.");
	}

	private void validateRole(String role) {
		if(Arrays.stream(Role.values()).noneMatch(r -> r.name().equalsIgnoreCase(role))) {
			throw new InvalidRoleException("Invalid role: " + role);
		}
	}

}

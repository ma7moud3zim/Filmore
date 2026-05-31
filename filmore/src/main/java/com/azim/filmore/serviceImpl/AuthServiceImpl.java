package com.azim.filmore.serviceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.azim.filmore.dao.UserRepository;
import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.EmailValidationResponse;
import com.azim.filmore.dto.response.LoginResponse;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.entity.User;
import com.azim.filmore.enums.Role;
import com.azim.filmore.exception.AccountDeactivatedException;
import com.azim.filmore.exception.BadCredentialsException;
import com.azim.filmore.exception.EmailAlreadyExistsException;
import com.azim.filmore.exception.EmailNotVerifiedException;
import com.azim.filmore.exception.InvalidTokenException;
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
		user.setEmailVerified(false);
		String verificationToken = UUID.randomUUID().toString();
		user.setVerificationToken(verificationToken);
		user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
		userRepository.save(user);
		
		emailService.sendVerificationEmail(userRequest.getEmail(), verificationToken);
		
		return new MessageResponse("Registeration successfull! Please check your email to verify your account.");
	}

	@Override
	public LoginResponse login(String email, String password) {
		User user = userRepository.
				findByEmail(email)
				.filter(u-> passwordEncoder.matches(password, u.getPassword()))
				.orElseThrow(()-> new BadCredentialsException("Invalid credentials"));
		if(!user.isActive()) {
			throw new AccountDeactivatedException("Your Account is not active from now. Please contact us for assistance.");
		}
		if(!user.isEmailVerified()) {
			throw new EmailNotVerifiedException("Please verify your email first. Check you mailbox");
		}
		final String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
		LoginResponse loginResponse = new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
		
		return loginResponse;
	}

	@Override
	public EmailValidationResponse validateEmail(String email) {
		boolean exists = userRepository.existsByEmail(email);
		return new EmailValidationResponse(exists, !exists);
	}

	@Override
	public MessageResponse verifyEmail(String token) {
		User user = userRepository.findByVerificationToken(token)
				.orElseThrow(() -> new EmailNotVerifiedException("Invalid or expired validation token"));
		if(user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(Instant.now())) {
			throw new InvalidTokenException("Invalid or expired validation token");
		}
		user.setEmailVerified(true);
		user.setVerificationToken(null);
		user.setVerificationTokenExpiry(null);
		userRepository.save(user);
		return new MessageResponse("Email verified successfully! you can now login.");
	}

}

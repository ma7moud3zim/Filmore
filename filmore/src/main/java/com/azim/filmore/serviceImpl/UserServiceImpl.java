package com.azim.filmore.serviceImpl;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.azim.filmore.dao.UserRepository;
import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.UserResponse;
import com.azim.filmore.entity.User;
import com.azim.filmore.enums.Role;
import com.azim.filmore.exception.EmailAlreadyExistsException;
import com.azim.filmore.exception.InvalidRoleException;
import com.azim.filmore.service.EmailService;
import com.azim.filmore.service.UserService;
import com.azim.filmore.util.PaginationUtils;
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

	@Override
	public MessageResponse updateUser(Long id, UserRequest userRequest) {
		User user = serviceUtils.getUserByIdOrThrow(id);
		
		ensureNotLastActiveAdmin(user);
		validateRole(userRequest.getRole());
		user.setFullName(userRequest.getFullName());
		user.setRole(Role.valueOf(userRequest.getRole().toLowerCase()));
		userRepository.save(user);
		return new MessageResponse("User updated successfully");
		
	}

	private void ensureNotLastActiveAdmin(User user) {
		if(user.isActive() && user.getRole().equals(Role.ADMIN) ) {
			long activeAdminCount = userRepository.countByRoleAndActive(Role.ADMIN, true);
			if(activeAdminCount <= 1) {
				throw new RuntimeException("Cannot deactivate last active admin");
			}
		}
		
	}

	@Override
	public PageResponse<UserResponse> getUsers(int page, int size, String search) {
		Pageable pageable = PaginationUtils.createPageRequest(page, size,"id");
		Page<User> userPage; 
		if(search != null && !search.trim().isEmpty()) {
			userPage = userRepository.searchUsers(search.trim(), pageable);
		}else {
			userPage = userRepository.findAll(pageable);
		}
		return PaginationUtils.toPageResponse(userPage, UserResponse::fromEntity);
	}

}

package com.azim.filmore.service;

import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.UserResponse;

public interface UserService {

	MessageResponse createUser(UserRequest userRequest);

	MessageResponse updateUser(Long id, UserRequest userRequest);

	PageResponse<UserResponse> getUsers(int page, int size, String search);


	MessageResponse deleteUser(Long id, String currentUserEmail);

	MessageResponse toggleUserStatus(Long id, String currentUserEmail);

}

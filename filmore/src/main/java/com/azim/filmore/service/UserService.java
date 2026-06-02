package com.azim.filmore.service;

import com.azim.filmore.dto.request.UserRequest;
import com.azim.filmore.dto.response.MessageResponse;

public interface UserService {

	MessageResponse createUser(UserRequest userRequest);

}

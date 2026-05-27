package com.azim.filmore.dto.request;

import lombok.Data;

@Data
public class UserRequest {
	
	private String email;
	private String password;
	private String fullName;
	private String lastrole;
	private Boolean active;
	
	
}

package com.azim.filmore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResopnse {
	private String token;
	private String email;
	private String fullName;
	private String role;
}

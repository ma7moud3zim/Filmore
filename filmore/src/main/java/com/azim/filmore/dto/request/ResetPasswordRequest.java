package com.azim.filmore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {
	
	@NotBlank
	private String token;
	
	@NotBlank
	@Size(min = 6, message ="Minimum valid password lenghth is 6 characters")
	private String newPassword;
	
}

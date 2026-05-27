package com.azim.filmore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
	
	@NotBlank(message = "Current Password is required")
	private String currentPassword;
	
	
	@NotBlank(message = "new Password is required")
	private String newPassword;
}

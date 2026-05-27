package com.azim.filmore.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String email;
	private String fullName;
	private String role;
	private Boolean active;
	private Instant createdAt;
	private Instant updatedAt;
}

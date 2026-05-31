package com.azim.filmore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class EmailValidationResponse {
	
	private boolean exists;
	private boolean available;

}

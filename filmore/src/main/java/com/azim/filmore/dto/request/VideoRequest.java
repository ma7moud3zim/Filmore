package com.azim.filmore.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VideoRequest {
	
	@NotBlank(message = "Title is required")
	private String title;
	
	@NotBlank(message = "Description is required")
	@Size(max=4000, message = "Description must be less than 4000 characters")
	private String description;
	
	private Integer year;
	private String rating;
	private Integer duration;
	private String src;
	private String poster;
	private Boolean published;
	private List<String> categories;
} 

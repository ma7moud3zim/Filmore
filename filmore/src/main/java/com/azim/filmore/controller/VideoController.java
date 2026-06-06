package com.azim.filmore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azim.filmore.dto.request.VideoRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.service.VideoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/videos")
public class VideoController {
	
	@Autowired
	private VideoService videoService;
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin")
	public ResponseEntity<MessageResponse> createVideoByAdmin(@Valid @RequestBody VideoRequest videoRequest) {
		return ResponseEntity.ok(videoService.createVideoByAdmin(videoRequest));
	}
}

package com.azim.filmore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azim.filmore.dto.request.VideoRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.VideoResponse;
import com.azim.filmore.dto.response.VideoStatsResponse;
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
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<PageResponse<VideoResponse>> getAllAdminVideos(
			@RequestParam(defaultValue="0") int page, 
			@RequestParam(defaultValue="10") int size,
			@RequestParam(required=false) String search){
		return ResponseEntity.ok(videoService.getAllAdminVideos(page, size, search));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/admin/{id}")
	public ResponseEntity<MessageResponse> updateVideoByAdmin(@PathVariable Long id,
			@Valid @RequestBody VideoRequest videoRequest) {
		return ResponseEntity.ok(videoService.updateVideoByAdmin(id, videoRequest));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/admin/{id}")
	public ResponseEntity<MessageResponse> deleteVideoByAdmin(@PathVariable Long id) {
		return ResponseEntity.ok(videoService.deleteVideoByAdmin(id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/admin/{id}/publish")
	public ResponseEntity<MessageResponse> toggleVideoPublishStatusByAdmin(@PathVariable Long id
			,@RequestParam boolean value) {
		return ResponseEntity.ok(videoService.toggleVideoPublishStatusByAdmin(id, value));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/stats")	
	public ResponseEntity<VideoStatsResponse> getAdminStats(){
		return ResponseEntity.ok(videoService.getAdminStats());
	}
	
	@GetMapping("/published")
	public ResponseEntity<PageResponse<VideoResponse>> getPublishedVideos(
			@RequestParam(defaultValue="0") int page, 
			@RequestParam(defaultValue="10") int size,
			@RequestParam(required=false) String search,
			Authentication auth) {
		String email = auth.getName();
		return ResponseEntity.ok(videoService.getPublishedVideos(page, size, search,email));
	}
	
	@GetMapping("/featured")
	public ResponseEntity<List<VideoResponse>> getFeaturedVideos() {
		return ResponseEntity.ok(videoService.getFeaturedVideos());
	}
	
	
	
}










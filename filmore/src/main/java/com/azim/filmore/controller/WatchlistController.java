package com.azim.filmore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.VideoResponse;
import com.azim.filmore.service.WatchlistService;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

	
	@Autowired
	private WatchlistService watchlistService;
	
	
	@PostMapping("/{videoId}")
	public ResponseEntity<MessageResponse> addToWatchlist(@PathVariable Long videoId, Authentication auth) {
		String email = auth.getName();
		return ResponseEntity.ok(watchlistService.addToWatchlist(videoId, email));
	}
	
	@DeleteMapping("/{videoId}")
	public ResponseEntity<MessageResponse>removeFromWatchlist(@PathVariable Long videoId, Authentication auth) {
		String email = auth.getName();
		return ResponseEntity.ok(watchlistService.removeFromWatchlist(videoId, email));
	}
	
	
	@GetMapping
	public ResponseEntity<PageResponse<VideoResponse>> getWatchlist(
			@RequestParam(defaultValue="0") int page, 
			@RequestParam(defaultValue="10") int size,
			@RequestParam(required=false) String search,
			Authentication auth) {
		String email = auth.getName();
		
		PageResponse<VideoResponse> response = watchlistService.getWatchlist(email, page, size, search);
		return ResponseEntity.ok(response);
	}
	
}

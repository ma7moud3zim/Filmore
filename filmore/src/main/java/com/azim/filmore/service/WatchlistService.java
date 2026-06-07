package com.azim.filmore.service;



import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.VideoResponse;

public interface WatchlistService {

	MessageResponse addToWatchlist(Long videoId, String email);

	MessageResponse removeFromWatchlist(Long videoId, String email);

	PageResponse<VideoResponse> getWatchlist( String email, int page, int size, String search);

}

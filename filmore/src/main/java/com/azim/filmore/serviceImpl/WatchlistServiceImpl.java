package com.azim.filmore.serviceImpl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azim.filmore.dao.UserRepository;
import com.azim.filmore.dao.VideoRepository;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.VideoResponse;
import com.azim.filmore.entity.User;
import com.azim.filmore.entity.Video;
import com.azim.filmore.service.WatchlistService;
import com.azim.filmore.util.PaginationUtils;
import com.azim.filmore.util.ServiceUtils;


@Service
public class WatchlistServiceImpl implements WatchlistService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VideoRepository videRepository;
	
	@Autowired
	private ServiceUtils serviceUtils;
	
	
	
	@Override
	public MessageResponse addToWatchlist(Long videoId, String email) {
		User user = serviceUtils.getUserByEmailOrThrow(email);
		Video video = serviceUtils.getVideoByIdOrThrow(videoId);
		user.addToWatchlist(video);
		userRepository.save(user);
		return new MessageResponse("Video added to watchlist successfully.");
	}



	@Override
	public MessageResponse removeFromWatchlist(Long videoId, String email) {
		User user = serviceUtils.getUserByEmailOrThrow(email);
		Video video = serviceUtils.getVideoByIdOrThrow(videoId);
		user.removeFromWatchlist(video);
		userRepository.save(user);
		return new MessageResponse("Video removed from watchlist successfully.");
	}



	@Override
	public PageResponse<VideoResponse> getWatchlist(String email, int page, int size, String search) {
		
		User user = serviceUtils.getUserByEmailOrThrow(email);
		Pageable pageable = PaginationUtils.createPageRequest(page, size);
		Page<Video> videoPage;
		if(search != null && !search.trim().isEmpty()) {
			videoPage = userRepository.searhWatchlistByUserId(user.getId(), search.trim(), pageable);
		}else {
			videoPage = userRepository.findByWatchlistUserId(user.getId(), pageable);
		}
		
		return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
	}

	
}

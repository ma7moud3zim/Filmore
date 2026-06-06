package com.azim.filmore.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.azim.filmore.dao.UserRepository;
import com.azim.filmore.dao.VideoRepository;
import com.azim.filmore.dto.request.VideoRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.VideoResponse;
import com.azim.filmore.entity.Video;
import com.azim.filmore.service.VideoService;
import com.azim.filmore.util.PaginationUtils;
import com.azim.filmore.util.ServiceUtils;

@Service
public class VideoServiceImpl implements VideoService {
	
	@Autowired
	private VideoRepository videoRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ServiceUtils serviceUtils;
	
	
	@Override
	public MessageResponse createVideoByAdmin(VideoRequest videoRequest) {
		Video video = new Video();
		video.setTitle(videoRequest.getTitle());
		video.setDescription(videoRequest.getDescription());
		video.setYear(videoRequest.getYear());
		video.setRating(videoRequest.getRating());
		video.setDuration(videoRequest.getDuration());
		video.setSrcUuid(videoRequest.getSrc());
		video.setPosterUuid(videoRequest.getPoster());
		video.setPublished(videoRequest.isPublished());
		video.setCategories(videoRequest.getCategories()!=null?videoRequest.getCategories():List.of());
		
		videoRepository.save(video);
		
		return new MessageResponse("Video created successfully");
	}

	@Override
	public PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search) {
		Pageable pageable = PaginationUtils.createPageRequest(page, size,"id");
		Page<Video> videoPage;
		if(search != null && !search.trim().isEmpty()) {
			videoPage = videoRepository.searchVideos(search.trim(), pageable);
		}else {
			videoPage = videoRepository.findAll(pageable);
		}
		return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
	}

}

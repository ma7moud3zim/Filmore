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
import com.azim.filmore.dto.response.VideoStatsResponse;
import com.azim.filmore.entity.Video;
import com.azim.filmore.service.VideoService;
import com.azim.filmore.util.PaginationUtils;
import com.azim.filmore.util.ServiceUtils;

import jakarta.validation.Valid;

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

	@Override
	public MessageResponse updateVideoByAdmin(Long id, @Valid VideoRequest videoRequest) {
		Video video = new Video();
		video.setId(id);
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
		
		return new MessageResponse("Video updated successfully");
	}

	@Override
	public MessageResponse deleteVideoByAdmin(Long id) {
		if(!videoRepository.existsById(id)) {
			return new MessageResponse("Video not found");
		}
		videoRepository.deleteById(id);
		return new MessageResponse("Video deleted successfully");
	}

	@Override
	public MessageResponse toggleVideoPublishStatusByAdmin(Long id, boolean status) {
		Video video = serviceUtils.getVideoByIdOrThrow(id);
		if(video == null) {
			return new MessageResponse("Video not found");
		}
		video.setPublished(status);
		videoRepository.save(video);
		return new MessageResponse("Video published status updated successfully");
	}

	@Override
	public VideoStatsResponse getAdminStats() {
		long totalVideos = videoRepository.count();
		long published = videoRepository.countPublished();
		long totalDuration = videoRepository.getTotalDuration();
		return new VideoStatsResponse(totalVideos, published, totalDuration);
	}
	
	

}

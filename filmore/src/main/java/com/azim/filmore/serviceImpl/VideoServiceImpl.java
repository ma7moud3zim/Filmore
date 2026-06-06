package com.azim.filmore.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azim.filmore.dao.VideoRepository;
import com.azim.filmore.dto.request.VideoRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.entity.Video;
import com.azim.filmore.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Override
	public MessageResponse createVideoByAdmin(VideoRequest videoRequest) {
		Video video = new Video();
		video.setTitle(videoRequest.getTitle());
		video.setDescription(videoRequest.getDescription());
		video.setYear(videoRequest.getYear());
		video.setRating(videoRequest.getRating());
		video.setDuration(videoRequest.getDuration());
		video.setPublished(videoRequest.isPublished());
		video.setCategories(videoRequest.getCategories());
		video.setSrcUuid(videoRequest.getSrc());
		video.setPosterUuid(videoRequest.getPoster());
		video.setPublished(videoRequest.isPublished());
		video.setCategories(videoRequest.getCategories()!=null?videoRequest.getCategories():List.of());
		
		videoRepository.save(video);
		
		return new MessageResponse("Video created successfully");
	}

}

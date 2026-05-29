package com.azim.filmore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azim.filmore.dao.UserRepository;
import com.azim.filmore.dao.VideoRepository;
import com.azim.filmore.entity.User;
import com.azim.filmore.entity.Video;
import com.azim.filmore.exception.ResourceNotFoundException;



@Component
public class ServiceUtils {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VideoRepository videoRepository;
	
	
	public User getUserByEmailOrThrow(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found: "+email));
	}
	
	public User getUserByIdOrThrow(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: "+id));
	}
	
	public Video getVideoByIdOrThrow(Long id) {
		return videoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Video not found: "+id));
	}
	
	
}
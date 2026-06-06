package com.azim.filmore.service;

import com.azim.filmore.dto.request.VideoRequest;
import com.azim.filmore.dto.response.MessageResponse;
import com.azim.filmore.dto.response.PageResponse;
import com.azim.filmore.dto.response.VideoResponse;

import jakarta.validation.Valid;

public interface VideoService {

	MessageResponse createVideoByAdmin(VideoRequest videoRequest);

	PageResponse<VideoResponse> getAllAdminVideos(int page, int size, String search);

	MessageResponse updateVideoByAdmin(Long id, @Valid VideoRequest videoRequest);

}

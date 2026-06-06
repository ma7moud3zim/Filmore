package com.azim.filmore.service;

import com.azim.filmore.dto.request.VideoRequest;
import com.azim.filmore.dto.response.MessageResponse;

import jakarta.validation.Valid;

public interface VideoService {

	MessageResponse createVideoByAdmin(VideoRequest videoRequest);

}

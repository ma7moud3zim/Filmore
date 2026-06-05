package com.azim.filmore.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	String storeVideoFile(MultipartFile file);

}

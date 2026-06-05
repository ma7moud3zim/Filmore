package com.azim.filmore.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azim.filmore.service.FileUploadService;

@RestController
@RequestMapping("/api/file")
public class FileUploadController {

	
	@Autowired
	private FileUploadService fileUploadService;
	
	@PostMapping("/upload/video")
	public ResponseEntity<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file) {
		String uuid = fileUploadService.storeVideoFile(file);
		return ResponseEntity.ok(buildUploadResponse(uuid,file));
	}

	private Map<String, String> buildUploadResponse(String uuid, MultipartFile file) {
		Map<String, String> response = new HashMap<>();
		response.put("uuid" , uuid);
		response.put("fileName",file.getOriginalFilename());
		response.put("size", String.valueOf(file.getSize()));
		return response;
	}
}

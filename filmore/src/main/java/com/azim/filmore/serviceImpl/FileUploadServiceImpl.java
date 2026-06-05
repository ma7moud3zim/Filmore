package com.azim.filmore.serviceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azim.filmore.service.FileUploadService;
import com.azim.filmore.util.FileHandlerUtil;

import jakarta.annotation.PostConstruct;


@Service
public class FileUploadServiceImpl implements FileUploadService {

	private Path videoStorageLocation;
	private Path imageStorageLocation;
	
	@Value("${file.upload.video-dir:uploads/videos}")
	private String videoDir;
	
	@Value("${file.upload.video-dir:uploads/images}")
	private String imageDir;
	
	@PostConstruct
	public void init() {
		videoStorageLocation = Paths.get(videoDir).toAbsolutePath().normalize();
		imageStorageLocation = Paths.get(imageDir).toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(videoStorageLocation);
			Files.createDirectories(imageStorageLocation);
		}catch(Exception e) {
			throw new RuntimeException("Could not create the directory where the uploaded files will be stored", e);
		}
		
	}
	
	
	@Override
	public String storeVideoFile(MultipartFile file) {
		return sotreFile(file, videoStorageLocation);
	}


	private String sotreFile(MultipartFile file, Path storageLocation) {
		String FileExtension = FileHandlerUtil.extractFileExtension(file.getOriginalFilename());
		String uuid = UUID.randomUUID().toString();
		String fileName = uuid + FileExtension;
		
		try {
			if(file.isEmpty()) {
				throw new RuntimeException("Failed to save empty file " + fileName);
			}
			Path targetLocation = storageLocation.resolve(fileName);
			Files.copy(file.getInputStream(),  targetLocation, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(file.getInputStream(), videoStorageLocation.resolve(fileName));
			return uuid;
		} catch (Exception e) {
			throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
		}
	}

}










package com.azim.filmore.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.azim.filmore.service.FileUploadService;
import com.azim.filmore.util.FileHandlerUtil;

import jakarta.annotation.PostConstruct;


@Service
public class FileUploadServiceImpl implements FileUploadService {

	private Path videoStorageLocation;
	private Path imageStorageLocation;
	
	@Value("${file.upload.video-dir:uploads/videos}")
	private String videoDir;
	
	@Value("${file.upload.image-dir:uploads/images}")
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
		return storeFile(file, videoStorageLocation);
	}

	@Override
	public String storeImageFile(MultipartFile file) {
		return storeFile(file, imageStorageLocation);
	}

	private String storeFile(MultipartFile file, Path storageLocation) {
		String FileExtension = FileHandlerUtil.extractFileExtension(file.getOriginalFilename());
		String uuid = UUID.randomUUID().toString();
		String fileName = uuid + FileExtension;
		
		try {
			if(file.isEmpty()) {
				throw new RuntimeException("Failed to save empty file " + fileName);
			}
			Path targetLocation = storageLocation.resolve(fileName);
			Files.copy(file.getInputStream(),  targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return uuid;
		} catch (Exception e) {
			throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
		}
	}


	@Override
	public ResponseEntity<Resource> serveVideo(String uuid, String rangeHeader) {
		try {
	        System.out.println(">>> serveVideo called");
	        System.out.println(">>> videoStorageLocation: " + videoStorageLocation);
	        System.out.println(">>> absolute path: " + videoStorageLocation.toAbsolutePath());
	        System.out.println(">>> uuid: " + uuid);
	        System.out.println(">>> rangeHeader: " + rangeHeader);
			Path filePath = FileHandlerUtil.findFileByUuid(videoStorageLocation, uuid);
			Resource resource = FileHandlerUtil.createFullResource(filePath);
			String fileName = resource.getFilename();
			String contentType = FileHandlerUtil.detectVideoContentType(fileName);
			long fileLength = resource.contentLength();
			if(isFullContentRequest(rangeHeader)) {
				return buildFullVideoResponse(resource, contentType, fileName,fileLength);
			}
			return buildPartialVideoResponse(filePath, rangeHeader, contentType, fileLength);
			
		}catch(Exception e) {
	        System.out.println(">>> ERROR: " + e.getMessage());
	        e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	
	}

	private boolean isFullContentRequest(String rangeHeader) {
		return rangeHeader == null || rangeHeader.isEmpty();
	}


	private ResponseEntity<Resource> buildFullVideoResponse(Resource resource, String contentType, String fileName,long fileLength) {
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"") 
	            .header(HttpHeaders.ACCEPT_RANGES, "bytes")
	            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
	            .body(resource);
	}

	private ResponseEntity<Resource> buildPartialVideoResponse(Path filePath, String rangeHeader,
			String contentType, long fileLength) throws IOException{
		long[] range = FileHandlerUtil.parseRangeHeader(rangeHeader, fileLength);
		long rangeStart = range[0];
		long rangeEnd = range[1];
		if(!isValidRange(rangeStart, rangeEnd, fileLength)) {
			return buildRnageNotSatisfiableResponse(fileLength);
		}
		long contentLength = rangeEnd - rangeStart + 1;
		Resource rangeResource = FileHandlerUtil.createRangeResource(filePath, rangeStart, contentLength);
		
		return ResponseEntity.status(206)
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline ; filename=\"" + filePath.getFileName() + "\"")
				.header(HttpHeaders.ACCEPT_RANGES, "bytes")
				.header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength)
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
				.body(rangeResource);
	}



	private boolean isValidRange(long rangeStart, long rangeEnd, long fileLength) {
		return rangeStart >= 0 && rangeStart <= rangeEnd && rangeEnd < fileLength;
	}
	
	private ResponseEntity<Resource> buildRnageNotSatisfiableResponse(long fileLength) {
		return ResponseEntity.status(416)
				.header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileLength)
				.build();
	}


	@Override
	public ResponseEntity<Resource> serveImage(String uuid, String rangeHeader) {
		try {
			Path filePath= FileHandlerUtil.findFileByUuid(imageStorageLocation, uuid);
			Resource resource = FileHandlerUtil.createFullResource(filePath);
			String fileName = resource.getFilename();
			String contentType = FileHandlerUtil.detectImageContentType(fileName);
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
					.body(resource);
			
		}catch(Exception ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
}










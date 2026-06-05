package com.azim.filmore.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


public class FileHandlerUtil {
	
	private FileHandlerUtil() {
		
	}
	
	public static String extractFileExtension(String fileName) {
		String fileExtension = "";
		
		if (fileName != null && fileName.contains(".")) {
			fileExtension = fileName.substring(fileName.lastIndexOf("."));
		}
		return fileExtension;
	}
	
	public static Path findFileByUuid(Path directory, String uuid) throws Exception {
		return Files.list(directory)
				.filter(path -> path.getFileName().toString().startsWith(uuid))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("File not found for uuid: " + uuid));
	}
	
	public static String detectVideoContentType(String fileName) {
		if(fileName == null) return "video/mp4";
		if(fileName.endsWith(".webm")) return "video/webm";
		if(fileName.endsWith(".ogg")) return "video/ogg";
		if(fileName.endsWith(".mkv")) return "video/x-matroska";
		if(fileName.endsWith(".avi")) return "video/x-msvideo";
		if(fileName.endsWith(".mov")) return "video/quicktime";
		if(fileName.endsWith(".flv")) return "video/x-flv";
		if(fileName.endsWith(".wmv")) return "video/x-ms-wmv";
		if(fileName.endsWith(".m4v")) return "video/x-m4v";
		if(fileName.endsWith(".3gp")) return "video/3gpp";
		if(fileName.endsWith(".mpg") || fileName.endsWith(".mpeg")) return "video/mpeg";
		
		return "video/mp4";
		
	}
	
	public static String detectImageContentType(String fileName) {
		if(fileName == null) return "image/jpeg";
		if(fileName.endsWith(".png")) return "image/png";
		if(fileName.endsWith(".gif")) return "image/gif";
		if(fileName.endsWith(".webp")) return "image/webp";

		return "image/jpeg";
	}
	
	
	public static long[] parseRangeHeader(String rangeHeader, long fileLength) {
		String[] ranges = rangeHeader.replace("bytes=", "").split("-");
		long start = ranges.length > 0 ? Long.parseLong(ranges[0]) : 0;
		long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileLength - 1;
		return new long[] { start, end };
	}
	
	public static Resource createRangeResource(Path filPath, long start, long rangeLength) throws IOException {
		RandomAccessFile fileReader = new RandomAccessFile(filPath.toFile(), "r");
		fileReader.seek(start);
		
		InputStream partialContentStream = new InputStream() {
			private long totalBytesRead = 0;
			@Override
			public int read() throws IOException{
				if(totalBytesRead >= rangeLength) { 
					fileReader.close(); 
					return -1;
				}
				totalBytesRead++;
				return fileReader.read();
			}
			
			@Override
			public int read(byte[] b, int offset, int length) throws IOException {
				if(totalBytesRead >= rangeLength) {
					fileReader.close();
					return -1;
				}
				
				long remBytes = rangeLength - totalBytesRead;
				int BytesToRead = (int) Math.min(remBytes, length);
				int bytesRead = fileReader.read(b, offset, BytesToRead);
				if(bytesRead > 0) {
					totalBytesRead += bytesRead;
				}
				if(totalBytesRead >= rangeLength) {
					fileReader.close();
				}
				
				return bytesRead;
			}
			@Override
			public void close() throws IOException {
				fileReader.close();
			}
		};
		
		return new InputStreamResource(partialContentStream) {
			@Override
			public long contentLength() {
				return rangeLength;
			}
		};
		
	}
	
	public static Resource createFullResource(Path filePath) throws IOException{
		Resource resource = new UrlResource(filePath.toUri().toURL());
		if(!resource.exists() || !resource.isReadable()) {
			throw new IOException("File not found or not readable: " + filePath);
			
		}
		return resource;
	}
	
}

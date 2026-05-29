package com.azim.filmore.exception;


import java.time.Instant;
import java.util.Map;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger	logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String,Object>> handleBadCredentials(BadCredentialsException ex) {
		logger.warn("BadCredentials: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}
	
	@ExceptionHandler(AccountDeactivatedException.class)
	public ResponseEntity<Map<String,Object>> handleAccountDeactivatedException(BadCredentialsException ex) {
		logger.warn("AccountDeactivatedException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}
	
	@ExceptionHandler(EmailNotVerifiedException.class)
	public ResponseEntity<Map<String,Object>> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
		logger.warn("EmailNotVerifiedException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}
	
	@ExceptionHandler(InvalidRoleException.class)
	public ResponseEntity<Map<String,Object>> handleInvalidRoleException(InvalidRoleException ex) {
		logger.warn("InvalidRoleException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Map<String,Object>> handleInvalidTokenException(InvalidTokenException ex) {
		logger.warn("InvalidTokenException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		logger.warn("ResourceNotFoundException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}
	
	@ExceptionHandler(EmailSendingException.class)
	public ResponseEntity<Map<String,Object>> handleEmailSendingException(EmailSendingException ex) {
		logger.warn("EmailSendingException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Map<String,Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
		logger.warn("EmailAlreadyExistsException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}
	
	@ExceptionHandler(InvalidCredentialException.class)
	public ResponseEntity<Map<String,Object>> handleInvalidCredentialException(InvalidCredentialException ex) {
		logger.warn("InvalidCredentialException: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,Object>> handleValidationException(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("Invalid request");
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		return ResponseEntity.status(status).body(Map.of("timestamp" , Instant.now(),"status", status.value() ,"error" ,message));
	}
	
	@ExceptionHandler({AsyncRequestNotUsableException.class,ClientAbortException.class})
	public void handleClientAbort(Exception ex) {
		logger.debug("Client closed connection throw streaming (expected for video seeking/buffering: {})", ex.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,Object>> handleGeneric(Exception ex) {
		logger.error("Exception: {}", ex.getMessage(),ex);
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}
	
	public ResponseEntity<Map<String,Object>> buildResponse(HttpStatus status, String message) {
		Map<String, Object> body = Map.of("timestamp" , Instant.now(),"message", message);
		return ResponseEntity.status(status).body(body);
	}

	
}

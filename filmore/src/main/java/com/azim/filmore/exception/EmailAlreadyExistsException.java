package com.azim.filmore.exception;

public class EmailAlreadyExistsException extends RuntimeException{
	public EmailAlreadyExistsException(String message) {
		super(message);
	}
}

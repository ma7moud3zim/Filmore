package com.azim.filmore.exception;

public class AccountDeactivatedException extends RuntimeException{
	public AccountDeactivatedException(String message) {
		super(message);
	}
}

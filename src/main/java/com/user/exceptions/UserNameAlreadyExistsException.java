package com.user.exceptions;

public class UserNameAlreadyExistsException extends RuntimeException{
	public UserNameAlreadyExistsException(String msg) {
		super(msg);
	}
}

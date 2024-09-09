package com.user.exceptions;

public class CustomerIdMismatchException extends RuntimeException{
	public CustomerIdMismatchException(String msg) {
		super(msg);
	}
}
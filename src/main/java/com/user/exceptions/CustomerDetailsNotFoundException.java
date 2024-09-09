package com.user.exceptions;

public class CustomerDetailsNotFoundException extends RuntimeException{
    public CustomerDetailsNotFoundException(String msg) {
    	super(msg);
    }
}


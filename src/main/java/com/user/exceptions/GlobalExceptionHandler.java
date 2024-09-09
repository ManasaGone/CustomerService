package com.user.exceptions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserNameAlreadyExistsException.class)
	public ResponseEntity<String> exceptionHandler(UserNameAlreadyExistsException e){
	String s=e.getMessage();
	ResponseEntity<String> re=new ResponseEntity<String>(s,HttpStatus.CONFLICT);
	return re;
	}
	@ExceptionHandler(UserNameNotFoundException.class)
	public ResponseEntity<String> exceptionHandler(UserNameNotFoundException e){
	String s=e.getMessage();
	ResponseEntity<String> re=new ResponseEntity<String>(s,HttpStatus.NOT_FOUND);
	return re;
	}
	@ExceptionHandler(CustomerDetailsNotFoundException.class)
	public ResponseEntity<String> exceptionHandler(CustomerDetailsNotFoundException e){
	String s=e.getMessage();
	ResponseEntity<String> re=new ResponseEntity<String>(s,HttpStatus.NOT_FOUND);
	return re;
	}
	@ExceptionHandler(CustomerIdMismatchException.class)
	public ResponseEntity<String> exceptionHandler(CustomerIdMismatchException e){
	String s=e.getMessage();
	ResponseEntity<String> re=new ResponseEntity<String>(s,HttpStatus.BAD_REQUEST);
	return re;
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<Map<String,String>> handleMyException(MethodArgumentNotValidException ex)
	    {
	        Map<String,String> m=new LinkedHashMap<>();
	        List<ObjectError> oerrors=ex.getAllErrors();
	        oerrors.forEach(obj->{
	            FieldError ferror=(FieldError)obj;
	            String fname=ferror.getField();
	            String message=ferror.getDefaultMessage();
	            m.put(fname, message);
	        });
	        ResponseEntity<Map<String,String>> re=new ResponseEntity<Map<String,String>>(m,HttpStatus.BAD_REQUEST);
	        return re;
	    }
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleException1(ConstraintViolationException e){
		String s= e.getMessage();
		ResponseEntity<String> re=new ResponseEntity<String>(s,HttpStatus.BAD_REQUEST);
		return re;
	}

}


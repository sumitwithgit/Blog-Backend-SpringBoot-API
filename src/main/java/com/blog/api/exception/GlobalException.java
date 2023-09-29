package com.blog.api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetails> UserExceptionHandler(UserException ue,WebRequest req){
		
		ErrorDetails err=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(CategoryException.class)
	public ResponseEntity<ErrorDetails> CategoryExceptionHandler(CategoryException ue,WebRequest req){
		
		ErrorDetails err=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PostException.class)
	public ResponseEntity<ErrorDetails> PostExceptionHandler(PostException ue,WebRequest req){
		
		ErrorDetails err=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetails> ResourceNotFoundExceptionHandler(ResourceNotFoundException ue,WebRequest req){
		
		ErrorDetails err=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CommentException.class)
	public ResponseEntity<ErrorDetails> CommentExceptionHandler(CommentException ue,WebRequest req){
		
		ErrorDetails err=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me){
		Map<String, String> resp=new HashMap<String, String>();
		me.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName = ((FieldError)error).getField();
			String defaultMessage = error.getDefaultMessage();
			resp.put(fieldName, defaultMessage);
		});
		return new ResponseEntity<Map<String, String>>(resp,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> otherExceptionHandler(Exception ue,WebRequest req){
		
		ErrorDetails err=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
}

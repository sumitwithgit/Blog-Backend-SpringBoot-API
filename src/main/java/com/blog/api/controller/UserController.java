package com.blog.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.dtos.UserDto;
import com.blog.api.exception.UserException;
import com.blog.api.service.UserService;

@RestController
@RequestMapping("/api/users/user")
public class UserController {

	private Logger logger=LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws UserException{
		UserDto createUser = this.userService.createUser(userDto);
		logger.info(" : createUser");
		return new ResponseEntity<UserDto>(createUser,HttpStatus.CREATED);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("userId") Integer userId) throws UserException{
		UserDto updateUser = this.userService.updateUser(userDto, userId);
		logger.info(" : updateUser");
		return new ResponseEntity<UserDto>(updateUser,HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserByUserId(@PathVariable("userId") Integer userId) throws UserException{
		UserDto userDto = this.userService.getUserById(userId);
		logger.info(" : getUserByUserId");
		logger.info("user : {}",userDto.toString());
		return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() throws UserException
	{
		List<UserDto> allUsers = this.userService.getAllUsers();
		logger.info(" : getAllUsers");
		return new ResponseEntity<List<UserDto>>(allUsers,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable("userId") Integer userId) throws UserException
	{
		this.userService.deleteUser(userId);
		logger.info(" : deleteUserById");
		return new ResponseEntity<String>("User Deleted Successfully.",HttpStatus.OK);
	}
}

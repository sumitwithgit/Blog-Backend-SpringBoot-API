package com.blog.api.service;

import java.util.List;

import com.blog.api.dtos.UserDto;
import com.blog.api.exception.UserException;

public interface UserService {

	UserDto createUser(UserDto user) throws UserException;
	
	UserDto updateUser(UserDto user, Integer userId) throws UserException;
	
	UserDto getUserById(Integer userId) throws UserException;
	
	List<UserDto> getAllUsers() throws UserException;
	
	void deleteUser(Integer userId) throws UserException;
	
	
}

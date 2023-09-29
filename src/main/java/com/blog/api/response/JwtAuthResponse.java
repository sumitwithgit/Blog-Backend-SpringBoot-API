package com.blog.api.response;

import com.blog.api.dtos.UserDto;

import lombok.Data;

@Data
public class JwtAuthResponse {

	private String token;
	private String message;
	private UserDto user;
}

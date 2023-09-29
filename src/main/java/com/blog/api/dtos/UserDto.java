package com.blog.api.dtos;


import java.util.HashSet;
import java.util.Set;

import lombok.Data;



@Data
public class UserDto {

	private int id;
	private String name;
	private String email;
	private String password;
	private String about;
	private Set<RoleDto> roles=new HashSet<>();
	
}

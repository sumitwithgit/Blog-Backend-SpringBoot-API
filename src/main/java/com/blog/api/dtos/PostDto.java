package com.blog.api.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PostDto {

	private Integer id;
	private String title;
	private String content;
	private String imageName;
	private LocalDateTime postedDate;
	private CategoryDto category;
	private UserDto user;
	private List<CommentDto> comment=new ArrayList<CommentDto>();
}

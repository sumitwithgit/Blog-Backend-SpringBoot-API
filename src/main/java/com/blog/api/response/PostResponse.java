package com.blog.api.response;

import java.util.List;

import com.blog.api.dtos.PostDto;

import lombok.Data;

@Data
public class PostResponse {

	private List<PostDto> content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	
	private boolean lastPage;
}

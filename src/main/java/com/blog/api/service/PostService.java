package com.blog.api.service;

import java.util.List;


import com.blog.api.dtos.PostDto;
import com.blog.api.exception.CategoryException;
import com.blog.api.exception.PostException;
import com.blog.api.exception.UserException;
import com.blog.api.response.PostResponse;

public interface PostService {

	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) throws PostException, UserException, CategoryException;
	
	PostDto updatePost(PostDto postDto,Integer postId) throws PostException, UserException, CategoryException;
	
	void deletePost(Integer postId) throws PostException;
	
	PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sort, String sortDir) throws PostException;
	
	PostDto getPostById(Integer postId) throws PostException;
	
	List<PostDto> getPostsByCategory(Integer categoryId) throws PostException, CategoryException;
	
	List<PostDto> getPostsByUser(Integer userId) throws PostException, UserException;
	
	List<PostDto> searchPosts(String keyword) throws PostException;
}

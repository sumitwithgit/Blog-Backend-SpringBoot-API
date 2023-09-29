package com.blog.api.service;

import com.blog.api.dtos.CommentDto;
import com.blog.api.exception.CommentException;
import com.blog.api.exception.PostException;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto, Integer postId) throws CommentException, PostException;
	
	void deleteComment(Integer commentId) throws CommentException;
}

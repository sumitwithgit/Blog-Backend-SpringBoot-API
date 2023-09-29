package com.blog.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.dtos.CommentDto;
import com.blog.api.exception.CommentException;
import com.blog.api.exception.PostException;
import com.blog.api.response.ApiResponse;
import com.blog.api.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	private Logger logger=LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentService commentService;
	
	@PostMapping("/post/{postId}/comment")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable("postId") Integer postId) throws CommentException, PostException{
		CommentDto createComment = this.commentService.createComment(commentDto, postId);
		logger.info(" : createComment");
		return new ResponseEntity<CommentDto>(createComment, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/post/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable("commentId") Integer commentId) throws CommentException{
		this.commentService.deleteComment(commentId);
		ApiResponse res=new ApiResponse();
		res.setMessage("Comment Deleted successfully.");
		res.setStatusCode("204");
		logger.info(" : deleteComment");
		return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
	}
}

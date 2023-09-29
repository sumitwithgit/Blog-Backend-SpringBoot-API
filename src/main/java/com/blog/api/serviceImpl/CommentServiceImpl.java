package com.blog.api.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.dtos.CommentDto;
import com.blog.api.exception.CommentException;
import com.blog.api.exception.PostException;
import com.blog.api.modal.Comment;
import com.blog.api.modal.Post;
import com.blog.api.repository.CommentRepository;
import com.blog.api.repository.PostRepository;
import com.blog.api.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) throws CommentException, PostException {
		Post post = this.postRepository.findById(postId).orElseThrow(()-> new PostException("Post Not FOund With Id :"+postId));
		Comment comment = this.dtoToComment(commentDto);

		comment.setPost(post);
		Comment saveComment = this.commentRepository.save(comment);
		return this.commentToDto(saveComment);
	}

	@Override
	public void deleteComment(Integer commentId) throws CommentException {
		Comment comment = this.commentRepository.findById(commentId).orElseThrow(()-> new CommentException("Comment Not Found with id :"+commentId));
		this.commentRepository.delete(comment);
	}
	
	public Comment dtoToComment(CommentDto commentDto) {
		return this.modelMapper.map(commentDto, Comment.class);
	}
	
	public CommentDto commentToDto(Comment comment) {
		return this.modelMapper.map(comment, CommentDto.class);
	}
	

}

package com.blog.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.api.dtos.PostDto;
import com.blog.api.exception.CategoryException;
import com.blog.api.exception.PostException;
import com.blog.api.exception.UserException;
import com.blog.api.response.ApiResponse;
import com.blog.api.response.PostResponse;
import com.blog.api.service.FileService;
import com.blog.api.service.PostService;
import com.blog.api.utils.AppConstants;


@RestController
@RequestMapping("/api")
public class PostController {

	private Logger logger=LoggerFactory.getLogger(PostController.class);
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/user/{userId}/category/{categoryId}/posts/post")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable("userId") Integer userId, @PathVariable("categoryId") Integer categoryId) throws PostException, UserException, CategoryException{
		PostDto createPostDto = this.postService.createPost(postDto, userId, categoryId);
		logger.info(" : createPost");
		return new ResponseEntity<PostDto>(createPostDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsbyCategory(@PathVariable("categoryId") Integer categoryId) throws PostException, CategoryException{
		List<PostDto> allPosts = this.postService.getPostsByCategory(categoryId);
		logger.info(" : getPostsbyCategory");
		return new ResponseEntity<List<PostDto>>(allPosts,HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable("userId") Integer userId) throws PostException, UserException{
		List<PostDto> allPosts = this.postService.getPostsByUser(userId);
		logger.info(" : getPostsByUser");
		return new ResponseEntity<List<PostDto>>(allPosts,HttpStatus.OK);
	}
	
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
					@RequestParam(value="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
					@RequestParam(value="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
					@RequestParam(value="sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
					@RequestParam(value="sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
			) throws PostException{
		PostResponse allPost = this.postService.getAllPost(pageNumber,pageSize,sortBy,sortDir);
		logger.info(" : getAllPosts");
		return new ResponseEntity<PostResponse>(allPost,HttpStatus.OK);
	}
	
	@GetMapping("/posts/post/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable("postId") Integer postId) throws PostException{
		PostDto postDto = this.postService.getPostById(postId);
		logger.info(" : getPostById");
		return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
	}
	
	
	@GetMapping("/posts/post/searchPost/{keyword}")
	public ResponseEntity<List<PostDto>> searchPosts(@PathVariable("keyword") String keyword) throws PostException{
		List<PostDto> allSearchPosts = this.postService.searchPosts(keyword);
		logger.info(" : Registration");
		return new ResponseEntity<List<PostDto>>(allSearchPosts,HttpStatus.OK);
	}
	
	@DeleteMapping("/posts/post/{postId}")
	public ResponseEntity<ApiResponse> deletePostById(@PathVariable("postId") Integer postId) throws PostException{
		this.postService.deletePost(postId);
		ApiResponse res=new ApiResponse();
		res.setMessage("Post Deleted Successfully.");
		res.setStatusCode("204");
		logger.info(" : deletePostById");
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
	
	@PutMapping("/posts/post/{postId}")
	public ResponseEntity<PostDto> updatePostById(@RequestBody PostDto postDto ,@PathVariable("postId") Integer postId) throws PostException, UserException, CategoryException{
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		logger.info(" : updatePostById");
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,@PathVariable("postId") Integer postId) throws IOException, PostException, UserException, CategoryException{
		PostDto postDto = this.postService.getPostById(postId);
		String uploadImage = this.fileService.uploadImage(path, image);
		postDto.setImageName(uploadImage);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		logger.info(" : uploadPostImage");
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	@GetMapping(value="/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName,HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		logger.info(" : downloadImage");
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
}

package com.blog.api.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.api.dtos.PostDto;
import com.blog.api.exception.CategoryException;
import com.blog.api.exception.PostException;
import com.blog.api.exception.UserException;
import com.blog.api.modal.Category;
import com.blog.api.modal.Post;
import com.blog.api.modal.User;
import com.blog.api.repository.CategoryRepo;
import com.blog.api.repository.PostRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) throws PostException, UserException, CategoryException {
		Post post = this.dtoToPost(postDto);
		User user = this.userRepository.findById(userId).orElseThrow(()->new UserException("User Not Found With Id : "+userId));
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new CategoryException("Category Not found With Id : "+categoryId));
		post.setUser(user);
		post.setCategory(category);
		post.setImageName("default.png");
		post.setPostedDate(LocalDateTime.now());
		Post savePost = this.postRepository.save(post);
		return this.postToDto(savePost);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) throws PostException {
		PostDto postDto2 = this.getPostById(postId);
		Post post = this.dtoToPost(postDto2);
		post.setPostedDate(LocalDateTime.now());
		if(postDto.getTitle()!=null) {
			post.setTitle(postDto.getTitle());
		}
		if(postDto.getContent()!=null) {
			post.setContent(postDto.getContent());
		}
		if(postDto.getImageName()!=null) {
			post.setImageName(postDto.getImageName());
		}
		Post savePost = this.postRepository.save(post);
		return this.postToDto(savePost);
	}

	@Override
	public void deletePost(Integer postId) throws PostException {
		PostDto postDto = this.getPostById(postId);
		Post post = this.dtoToPost(postDto);
		this.postRepository.delete(post);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sort, String sortDir) throws PostException {
		Sort sortBy=(sortDir.equalsIgnoreCase("asc"))?Sort.by(sort).ascending():Sort.by(sort).descending();
		
		PageRequest p=PageRequest.of(pageNumber, pageSize, sortBy);
		Page<Post> pagePost = this.postRepository.findAll(p);
		List<Post> allPost = pagePost.getContent();
		List<PostDto> allPosts = this.allPostToDtos(allPost);
		
		PostResponse res=new PostResponse();
		res.setContent(allPosts);
		res.setPageNumber(pagePost.getNumber());
		res.setPageSize(pagePost.getSize());
		res.setTotalPages(pagePost.getTotalPages());
		res.setTotalElements(pagePost.getTotalElements());
		res.setLastPage(pagePost.isLast());
		
		if(allPosts.isEmpty() || allPosts.size()<=0) {
			throw new PostException("No Post Found.");
		}
		return res;
	}

	@Override
	public PostDto getPostById(Integer postId) throws PostException {
		Optional<Post> postOpt = this.postRepository.findById(postId);
		if(postOpt.isPresent()) {
			Post post = postOpt.get();
			return this.postToDto(post);
		}
		throw new PostException("Post Not Found With Id : "+postId);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) throws PostException, CategoryException {
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()->new CategoryException("Category Not found With Id : "+categoryId));
		List<Post> allPosts = this.postRepository.findByCategory(category);
		if(allPosts.size()<=0 || allPosts.isEmpty()) {
			throw new PostException("No Post Found With this category : "+category.getCategoryTitle());
		}
		return this.allPostToDtos(allPosts);
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) throws PostException, UserException {
		User user = this.userRepository.findById(userId).orElseThrow(()->new UserException("User Not Found With Id : "+userId));
		List<Post> allPosts = this.postRepository.findByUser(user);
		if(allPosts.size()<=0 || allPosts.isEmpty()) {
			throw new PostException("No Post Found With this User : "+user.getName());
		}
		return this.allPostToDtos(allPosts);
	}

	@Override
	public List<PostDto> searchPosts(String keyword) throws PostException {
		List<Post> allPosts = this.postRepository.searchPost(keyword);
		if(allPosts.size()<=0 || allPosts.isEmpty()) {
			throw new PostException("No Post Found With this Keyword : "+keyword);
		}
		return this.allPostToDtos(allPosts);
	}
	
	public PostDto postToDto(Post post) {
		return this.modelMapper.map(post, PostDto.class);
	}
	
	public Post dtoToPost(PostDto postDto) {
		return this.modelMapper.map(postDto, Post.class);
	}
	
	public List<Post> allDtosToPost(List<PostDto> allDtos){
		return allDtos.stream().map((postDto)->this.dtoToPost(postDto)).collect(Collectors.toList());
	}
	
	public List<PostDto> allPostToDtos(List<Post> allPosts){
		return allPosts.stream().map((post)->this.postToDto(post)).collect(Collectors.toList());
	}


}

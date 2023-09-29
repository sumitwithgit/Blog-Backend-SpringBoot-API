package com.blog.api.controller;


import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.api.config.TokenProvider;
import com.blog.api.dtos.UserDto;
import com.blog.api.exception.UserException;
import com.blog.api.modal.Role;
import com.blog.api.modal.User;
import com.blog.api.repository.RoleRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.JwtAuthRequest;
import com.blog.api.response.JwtAuthResponse;
import com.blog.api.service.CustomUserDetailService;
import com.blog.api.utils.AppConstants;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private Logger logger=LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private CustomUserDetailService customUserService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<JwtAuthResponse> createUserHandler(@RequestBody UserDto user) throws UserException{

		Optional<User> isUser = this.userRepository.findByEmail(user.getEmail());

		if(isUser.isEmpty()) {
			User newUser=new User();
			newUser.setEmail(user.getEmail());
			newUser.setName(user.getName());
			newUser.setAbout(user.getAbout());
			newUser.setPassword(passwordEncoder.encode(user.getPassword()));
			
			Role role = this.roleRepository.findById(AppConstants.ROLE_NORMAL).get();
			
			newUser.getRoles().add(role);
			
			this.userRepository.save(newUser);
			Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			String jwt = this.tokenProvider.generateToken(authentication);
			
			JwtAuthResponse res=new JwtAuthResponse();
			res.setToken(jwt);
			res.setMessage("Account Created Successfully.");
			res.setUser(this.modelMapper.map(newUser, UserDto.class));
			logger.info(" : Registration");
			return new ResponseEntity<JwtAuthResponse>(res,HttpStatus.ACCEPTED);
		}
			throw new UserException("User Already Exist with email "+user.getEmail());			
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<JwtAuthResponse> loginHandler(@RequestBody JwtAuthRequest req){
		String email=req.getUsername();
		String password=req.getPassword();
		
		Authentication authentication=authenticate(email, password);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
		User user = this.userRepository.findByEmail(email).get();
		JwtAuthResponse res=new JwtAuthResponse();
		res.setToken(jwt);
		res.setUser(this.modelMapper.map(user, UserDto.class));
		res.setMessage("Login Success.");
		logger.info(" : Login");
		return new ResponseEntity<JwtAuthResponse>(res,HttpStatus.ACCEPTED);
	}
	
	public Authentication authenticate(String username,String password)
	{
		UserDetails userDetails=this.customUserService.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid UserName");
		}
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Incorrect Password.");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	
}

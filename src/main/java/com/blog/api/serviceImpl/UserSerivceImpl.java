package com.blog.api.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.api.dtos.UserDto;
import com.blog.api.exception.UserException;
import com.blog.api.modal.User;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.UserService;

@Service
public class UserSerivceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDto createUser(UserDto userdto) throws UserException {
		User user = this.dtoToUser(userdto);
		User saveUser = this.userRepository.save(user);
		UserDto userDto2 = this.UserToDto(saveUser);
		return userDto2;
	}

	@Override
	public UserDto updateUser(UserDto userdto, Integer userId) throws UserException {
		UserDto getUserDto = getUserById(userId);
		User user = this.dtoToUser(getUserDto);
		if(userdto.getName()!=null) {
			user.setName(userdto.getName());			
		}
		if(userdto.getEmail()!=null) {
			throw new UserException("User Name/Email cann't be change. Cann't Editable");
//			user.setEmail(userdto.getEmail());			
		}
		if(userdto.getPassword()!=null) {
			user.setPassword(userdto.getPassword());			
		}
		if(userdto.getAbout()!=null) {
			user.setAbout(userdto.getAbout());			
		}
		User updatedUser = this.userRepository.save(user);
		return this.UserToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) throws UserException {
		Optional<User> optUser = this.userRepository.findById(userId);
		if(optUser.isPresent()) {
			User user = optUser.get();
			return this.UserToDto(user);
		}
		throw new UserException("User Not Found With User Id : "+userId);
	}

	@Override
	public List<UserDto> getAllUsers() throws UserException {
		List<User> allUsers = this.userRepository.findAll();
		if(allUsers.isEmpty() || allUsers.size()<=0) {
			throw new UserException("No User Found");
		}
		List<UserDto> allUserDto = this.allUserToDto(allUsers);
		return allUserDto;
	}

	@Override
	public void deleteUser(Integer userId) throws UserException {
		UserDto userDto = this.getUserById(userId);
		User user = this.dtoToUser(userDto);
		this.userRepository.delete(user);
	}
	
	public User dtoToUser(UserDto userdto) {
		User user=new User();
		user.setId(userdto.getId());
		user.setName(userdto.getName());
		user.setEmail(userdto.getEmail());
		user.setAbout(userdto.getAbout());
		user.setPassword(userdto.getPassword());
		return user;
	}

	public UserDto UserToDto(User user) {
		UserDto dto=new UserDto();
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setAbout(user.getAbout());
		return dto;
	}
	
	public List<UserDto> allUserToDto(List<User> users){
		return users.stream().map(user->this.UserToDto(user)).collect(Collectors.toList());
	}
	
	
	public List<User> allDtoToUser(List<UserDto> userDtos){
		return userDtos.stream().map(userDto->this.dtoToUser(userDto)).collect(Collectors.toList());
	}
}

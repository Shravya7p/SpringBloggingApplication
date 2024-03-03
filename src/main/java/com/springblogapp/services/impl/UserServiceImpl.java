package com.springblogapp.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springblogapp.config.AppConstants;
import com.springblogapp.entities.Role;
import com.springblogapp.entities.User;
import com.springblogapp.exceptions.ResourceNotFoundException;
import com.springblogapp.payloads.UserDto;
import com.springblogapp.repos.RoleRepo;
import com.springblogapp.repos.UserRepo;
import com.springblogapp.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
   
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		// TODO Auto-generated method stub
		User user = this.dtoToUser(userDto);
		User user1 = this.userRepo.save(user);
//		UserDto userDto1 = this.userToDto(user1);
//		return userDto;
		return this.userToDto(user1);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		// TODO Auto-generated method stub
		
		User user1 = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","id",userId));
		//user1.setId(userDto.getId());
		user1.setName(userDto.getName());
		user1.setEmail(userDto.getEmail());
		user1.setPassword(userDto.getPassword());
		user1.setAbout(userDto.getPassword());
		User updatedUser = this.userRepo.save(user1);
		UserDto userDto1 = this.userToDto(updatedUser);
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()->new ResourceNotFoundException("User ","Id",userId));
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		// TODO Auto-generated method stub
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user1 = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User ","Id",userId));
		this.userRepo.delete(user1);
	}
	
	private User dtoToUser(UserDto userDto) {
		//User user = new User();
		User user = this.modelMapper.map(userDto,User.class);
		//user.setId(userDto.getId());
		return user;
//		user.setId(userDto.getId());
//		user.setEmail(userDto.getEmail());
//		user.setName(userDto.getName());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
//		return user;
		
	}
	
	public UserDto userToDto(User user) {
//		UserDto userDto = new UserDto();
		UserDto userDto = this.modelMapper.map(user,UserDto.class);
		return userDto;
//		userDto.setId(user.getId());
//		userDto.setEmail(user.getEmail());
//		userDto.setName(user.getName());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
//		
//		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		// TODO Auto-generated method stub
		User user = this.modelMapper.map(userDto, User.class);
		
		//encoded the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		//roles
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		
		User newUser = this.userRepo.save(user);
		
		return this.modelMapper.map(newUser, UserDto.class);
	}
}

package com.springblogapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springblogapp.payloads.ApiResponse;
import com.springblogapp.payloads.UserDto;
import com.springblogapp.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
   //Post - create user
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto userDto1 = this.userService.createUser(userDto);
		return new ResponseEntity<>(userDto1,HttpStatus.CREATED);
	}
	//Put update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer userId){
		UserDto updatedUser = this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updatedUser);
	}
	//Delete delete user
	//ADMIN
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userId){
		this.userService.deleteUser(userId);
		//return new ResponseEntity(Map.of("message","user deleted successfully!"),HttpStatus.OK);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully!",true),HttpStatus.OK);
	}
	
	//GET users
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
		
	}
	
	//GET user by id
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId){
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}

}

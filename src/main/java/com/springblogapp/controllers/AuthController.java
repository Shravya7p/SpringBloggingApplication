package com.springblogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springblogapp.exceptions.ApiException;
import com.springblogapp.payloads.JwtAuthRequest;
import com.springblogapp.payloads.JwtAuthResponse;
import com.springblogapp.payloads.UserDto;
import com.springblogapp.security.JwtTokenHelper;
import com.springblogapp.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationMananger;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception{
		this.authenticate(request.getUsername(),request.getPassword());
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = this.jwtTokenHelper.generateToken(userDetails); /// inorder to generate return values press Ctrl+1
		
		JwtAuthResponse response = new JwtAuthResponse();
		response.setToken(token);
		return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);
		//return null;
	}
	
	private void authenticate(String username, String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
		 try {
			 this.authenticationMananger.authenticate(authenticationToken);
		 }
		 catch(BadCredentialsException e) {
			 System.out.println("Invalid Username and Password");
			 throw new ApiException("Invalid username and password!!");
		 }
			
	}
		
	//register new User
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
		
		UserDto registeredUser = this.userService.registerNewUser(userDto);
		return new ResponseEntity<>(registeredUser,HttpStatus.CREATED);
	}
	
	

}

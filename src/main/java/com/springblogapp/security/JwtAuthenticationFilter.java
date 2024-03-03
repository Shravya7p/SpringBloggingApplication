package com.springblogapp.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//get Token
		String requestHeader = request.getHeader("Authorization");
		
		//Bearer 234u390
		System.out.println("requestHeader is {} "+requestHeader);
		
		String username = null;
		
		String token = null;
		
		if(requestHeader != null && requestHeader.startsWith("Bearer"))
		{
			token = requestHeader.substring(7);
			try {
				username = this.jwtTokenHelper.getUsernameFromToken(token);
			}
			catch(ExpiredJwtException e) {
				System.out.println("Jwt Token has expired!!");
			}
			catch(MalformedJwtException e) {
				System.out.println("Jwt Token has expired!!");
			}
			
		}
		else {
			System.out.println("JWT Token does not begin with Bearer");
		}
		
		//once we get the token, validate
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				//everything is fine
				//do authentication
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				
			}
			else {
				System.out.println("Invalid JWT Token");
			}
			
		}
		else {
			System.out.println("username is null or context is not null");
		}
		
		filterChain.doFilter(request, response);
		
	}

}

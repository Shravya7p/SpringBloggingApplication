package com.springblogapp.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {
	
	public static final long JWT_TOKEN_VALIDITY=5*60*60;  // it is in milliseconds
	
	private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	
	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token,Claims::getSubject);
	}
	
	//retrieve expiration date from JWT Token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token,Claims::getExpiration);
	}
   
	public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	//for retrieving any information from token, we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		//Jwts.parser().verifyWith(secret)..parseClaimsJws(token);
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	
	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims = new HashMap<>();
		return doGenerateToken(claims,userDetails.getUsername());
	}
	
	//while creating token
	//1) Define claims of token like issuer, expiration date, subject and ID
	// 2) sign  the JWT Key using HS512 algorithm and secret key
	
	private String doGenerateToken(Map<String,Object> claims, String subject) {
//		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm. HS256 ;
//		SecretKey key = Keys. hmacShaKeyFor (“SECRET_KEY”.getBytes(StandardCharsets. UTF_8 ));
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())) 
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000))
				.signWith(SignatureAlgorithm.HS512,secret).compact();
	}
	
	//validate Token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}

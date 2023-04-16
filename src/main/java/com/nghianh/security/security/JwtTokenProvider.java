package com.nghianh.security.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The JwtTokenUtil is responsible for performing JWT operations like creation
 * and validation. It makes use of the io.jsonwebtoken.Jwts for achieving this.
 * 
 * @author Nguyen Huu Nghia
 *
 */

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration}")
	private long validityInMilliseconds;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// retrieve username from jwt
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// for retrieving any information from token we will need the secret key
	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	// check if the token has expired
	private Boolean isTokenExpired(String token) {
		// here it will check if the token has created before time limit. i.e validityInMilliseconds then will return true else false
		return extractExpiration(token).before(new Date());
	}

	// current claim is empty, can pass any other specific claims -> payload?
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

//	 while creating the token:
//	 1. Define claims of the token, like Issue, Expiration, Subject, and the ID 
//	 2. Sign the JWT using the HS256 algorithm and secret key. 
//	 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1) 
//	 compaction of the JWT to a URL-safe string

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()//
				.setClaims(claims)//
				.setSubject(subject) // the subject is the person who is being authenticated
				.setIssuedAt(new Date(System.currentTimeMillis()))//
				.setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}

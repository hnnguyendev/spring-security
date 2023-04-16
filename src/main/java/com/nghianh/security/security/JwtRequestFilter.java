package com.nghianh.security.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter
 * class. For any incoming request this Filter class gets executed. It checks if
 * the request has a valid JWT token. If it has a valid JWT Token then it sets
 * the Authentication in the context, to specify that the current user is
 * authenticated.
 */
//We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
//		final String authorizationHeader = request.getHeader("Authorization");
//		String username = null;
//		String jwt = null;
//		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
////			jwt = authorizationHeader.substring("Bearer ".length());
//			jwt = authorizationHeader.substring(7);
//			username = jwtTokenProvider.extractUsername(jwt);
//		}
		String jwt = getJwtFromRequest(request);
		String username = null;
		if (jwt != null) {
			username = jwtTokenProvider.extractUsername(jwt);
		}
		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenProvider.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				/*
				 * After setting the Authentication in the context, we specify that the current
				 * user is authenticated. So it passes the Spring Security Configurations
				 * successfully.
				 */
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		final String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7);
		}
		return null;
	}

}

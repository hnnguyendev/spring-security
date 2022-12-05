package com.nhnghia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nhnghia.security.CustomUserDetailsServiceImpl;
import com.nhnghia.security.JwtAuthenticationEntryPoint;
import com.nhnghia.security.JwtRequestFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		 * configure AuthenticationManagerBuilder so that it knows from where to load
		 * user for matching credentials
		 */
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.csrf().disable()
			// make sure we use stateless session; session won't be used to store user's state
			// No session will be created or used by spring security
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers("/auth/signup", 
						"/auth/signin", 
						"/auth/verify", 
						"/auth/me", 
						"/auth/email-reset-password", 
						"/auth/reset-password", 
						"/user/create")
				.permitAll()
				.antMatchers("/resources/**", "/assets/**").permitAll()
				.antMatchers("/v2/api-docs",
						"/configuration/ui",
						"/swagger-resources/**",
						"/configuration/security",
						"/swagger-ui.html",
						"/webjars/**")
				.permitAll()
				.antMatchers("/auth/admin/**", "/auth/role/save", "/auth/role/add-to-user").hasRole("ADMIN")
				.antMatchers("/user/hello").hasAnyRole("USER", "ADMIN")
				// all other requests need to be authenticated
				.anyRequest().authenticated();

		// If a user try to access a resource without authentication
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

		// Add a filter to validate the tokens with every request
		// jwtRequestFilter called before UsernamePasswordAuthenticationFilter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		//@formatter:on
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

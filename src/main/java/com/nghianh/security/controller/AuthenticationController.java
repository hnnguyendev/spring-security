package com.nghianh.security.controller;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nghianh.security.dto.AuthenticationRequest;
import com.nghianh.security.dto.ResetPasswordDto;
import com.nghianh.security.dto.UpdateUserDto;
import com.nghianh.security.entity.SystemRole;
import com.nghianh.security.entity.SystemUser;
import com.nghianh.security.service.AuthenticationService;

/**
 * 
 * Expose a POST API /auth/signin using the AuthenticationController. The POST
 * API gets username and password in the body - Using Spring
 * AuthenticationManager we authenticate the username and password. If the
 * credentials are valid, a JWT token is created using the jwtTokenProvider and
 * provided to the client.
 * 
 * @author nguyenhuunghia
 *
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/signup")
	public Map<String, Object> signup(@RequestBody SystemUser systemUser) {
		return authenticationService.signup(systemUser);
	}

	@PostMapping("/signin")
	public Map<String, Object> signin(@RequestBody AuthenticationRequest authenticationRequest) {
		return authenticationService.signin(authenticationRequest);
	}

	@GetMapping("/me")
	public Map<String, Object> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
		return authenticationService.getCurrentUser(userDetails);
	}

	@GetMapping("/verify")
	public Map<String, Object> verifyUser(@RequestParam String token) {
		return authenticationService.verifyUser(token);
	}

	@PostMapping("/email-reset-password")
	public Map<String, Object> sendResetPasswordEmail(@RequestBody SystemUser systemUser) {
		return authenticationService.sendResetPasswordEmail(systemUser);
	}

	@PostMapping("/reset-password")
	public Map<String, Object> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
		return authenticationService.resetPassword(resetPasswordDto);
	}

	@PostMapping("/edit-password")
	public Map<String, Object> editPassword(@RequestBody UpdateUserDto updateUserDto) {
		return authenticationService.editPassword(updateUserDto);
	}

	@PostMapping("/role/save")
	public ResponseEntity<SystemRole> saveRole(@RequestBody SystemRole systemRole) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(authenticationService.saveRole(systemRole));
	}

	@PostMapping("/role/add-to-user")
	public ResponseEntity<?> addRoleToUser(@RequestParam String username, @RequestParam String role) {
		authenticationService.addRoleToUser(username, role);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = { "/admin/hello" }, method = RequestMethod.GET)
	public String helloAdmin(Principal principal) {
		return "Hello admin: " + principal.getName();
	}

	@RequestMapping(value = { "/user/hello" }, method = RequestMethod.GET)
	public String helloUser(Principal principal) {
		return "Hello user: " + principal.getName();
	}

}

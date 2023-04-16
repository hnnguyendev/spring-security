package com.nghianh.security.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.nghianh.security.dto.AuthenticationRequest;
import com.nghianh.security.dto.ResetPasswordDto;
import com.nghianh.security.dto.UpdateUserDto;
import com.nghianh.security.entity.SystemRole;
import com.nghianh.security.entity.SystemUser;

public interface AuthenticationService {

	Map<String, Object> signup(SystemUser systemUser);

	Map<String, Object> signin(AuthenticationRequest authenticationRequest);
	
	Map<String, Object> getCurrentUser(UserDetails userDetails);

	SystemRole saveRole(SystemRole systemRole);

	void addRoleToUser(String username, String role);

	Map<String, Object> verifyUser(String token);

	Map<String, Object> sendResetPasswordEmail(SystemUser systemUser);

	Map<String, Object> resetPassword(ResetPasswordDto resetPasswordDto);

	Map<String, Object> editPassword(UpdateUserDto updateUserDto);

}

package com.nhnghia.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

import com.nhnghia.dto.AuthenticationRequest;
import com.nhnghia.dto.ResetPasswordDto;
import com.nhnghia.dto.UpdateUserDto;
import com.nhnghia.entity.SystemRole;
import com.nhnghia.entity.SystemUser;

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

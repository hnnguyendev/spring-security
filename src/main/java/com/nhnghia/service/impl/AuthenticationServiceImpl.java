package com.nhnghia.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnghia.dto.AuthenticationRequest;
import com.nhnghia.dto.ResetPasswordDto;
import com.nhnghia.dto.UpdateUserDto;
import com.nhnghia.entity.SystemRole;
import com.nhnghia.entity.SystemUser;
import com.nhnghia.repository.RoleRepository;
import com.nhnghia.repository.UserRepository;
import com.nhnghia.security.CustomUserDetailsServiceImpl;
import com.nhnghia.security.JwtTokenProvider;
import com.nhnghia.service.AuthenticationService;
import com.nhnghia.service.EmailService;
import com.nhnghia.util.TokenUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger LOGGER = LogManager.getLogger(AuthenticationServiceImpl.class);

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${app.user.token.expiration}")
	private long expiredLink;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public boolean checkEmailExist(String username) {
		return userRepository.findByUsername(username);

	}

	/**
	 * must have @Transactional to insert data to user_roles table
	 */
	@Override
	@Transactional
	public Map<String, Object> signup(SystemUser systemUser) {

		Map<String, Object> mapResult = new HashMap<>();

		String username = systemUser.getUsername();

		SystemUser theUser = userRepository.findByUsernameIgnoreCase(username);
		if (theUser == null) {
			SystemUser newUser = new SystemUser();
			newUser.setUsername(username);
			newUser.setPassword(passwordEncoder.encode(systemUser.getPassword()));
			newUser.setFirstname(systemUser.getFirstname());
			newUser.setLastname(systemUser.getLastname());
			newUser.setAvatar(systemUser.getAvatar());
			userRepository.save(newUser);
			this.addRoleToUser(username, "ROLE_USER");
			emailService.sendVerifyEmail(username);
			mapResult.put("msg", "Please verify you email");
			mapResult.put("status", true);
		} else {
			mapResult.put("msg", "Looks like you already have an account with that email address");
			mapResult.put("status", false);
		}
		return mapResult;
	}

	@Override
	public Map<String, Object> signin(AuthenticationRequest authenticationRequest) {
		try {
			String username = authenticationRequest.getUsername();

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword()));

			final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

			final String jwt = jwtTokenProvider.generateToken(userDetails);

			Map<String, Object> mapResult = new HashMap<>();
			mapResult.put("accessToken", jwt);
			mapResult.put("tokenType", "Bearer");
			mapResult.put("username", username);
			mapResult.put("role", userDetails.getAuthorities().stream().map(role -> role.getAuthority())
					.filter(Objects::nonNull).collect(Collectors.toList()));

			return mapResult;
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Incorrect username or password", e);
		}
	}

	@Override
	public Map<String, Object> getCurrentUser(UserDetails userDetails) {
		Map<String, Object> mapResult = new HashMap<>();
		mapResult.put("username", userDetails.getUsername());
		mapResult.put("roles", userDetails.getAuthorities().stream().map(role -> role.getAuthority())
				.filter(Objects::nonNull).collect(Collectors.toList()));
		return mapResult;
	}

	@Override
	public SystemRole saveRole(SystemRole systemRole) {
		return roleRepository.save(systemRole);
	}

	@Override
	@Transactional
	public void addRoleToUser(String username, String role) {
		SystemUser systemUser = userRepository.findByUsernameIgnoreCase(username);
		SystemRole newRole = roleRepository.findByRoleName(role);
		systemUser.getSystemRoles().add(newRole);

	}

	@Override
	public Map<String, Object> verifyUser(String token) {

		LOGGER.info("Start GET /verify");

		LOGGER.info("GET /verify PARAMS TOKEN:" + token);

		Map<String, Object> mapResult = new HashMap<>();

		try {
			if (StringUtils.isNotEmpty(token)) {
				String strToken = TokenUtil.decrypt(token, secretKey);
				String[] params = strToken.split("&");
				for (String param : params) {
					String[] value = param.split("=");
					if (value.length > 1) {
						if (param.contains("createDate")) {
							long currentTime = System.currentTimeMillis();
							long expired = Long.parseLong(value[1]) + expiredLink;
							if (currentTime > expired) {
								mapResult.put("msg", "Your verification link has expired");
								mapResult.put("status", false);
								return mapResult;
							}
						}
						if (param.contains("email")) {
							SystemUser systemUser = userRepository.findByUsernameIgnoreCase(value[1]);
							if (systemUser != null) {
								systemUser.setEnabled(true);
								userRepository.save(systemUser);
								mapResult.put("msg", "User verified successfully!");
								mapResult.put("status", true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			mapResult.put("msg", "Verify user failed!");
			mapResult.put("status", false);
			LOGGER.error("GET /verify ERROR MESSAGE: " + ExceptionUtils.getStackTrace(e));
		}

		LOGGER.info("End Service UserServiceImpl Function verifyUser");

		return mapResult;

	}

	@Override
	public Map<String, Object> sendResetPasswordEmail(SystemUser systemUser) {

		LOGGER.info("Begin Service UserServiceImpl Function sendResetPasswordEmail");

		Map<String, Object> mapResult = new HashMap<>();

		SystemUser theUser = userRepository.findByUsernameIgnoreCase(systemUser.getUsername());
		if (theUser != null) {
			emailService.sendResetPasswordEmail(systemUser.getUsername());
			mapResult.put("msg", "Email sent successfully!");
			mapResult.put("status", true);
		}

		LOGGER.info("End Service UserServiceImpl Function sendResetPasswordEmail");

		return mapResult;
	}

	@Override
	public Map<String, Object> resetPassword(ResetPasswordDto resetPasswordDto) {

		Map<String, Object> mapResult = new HashMap<>();

		try {
			if (StringUtils.isNotEmpty(resetPasswordDto.getToken())) {
				String strToken = TokenUtil.decrypt(resetPasswordDto.getToken(), secretKey);
				String[] params = strToken.split("&");
				for (String param : params) {
					String[] value = param.split("=");
					if (value.length > 1) {
						if (param.contains("createDate")) {
							long currentTime = System.currentTimeMillis();
							long expired = Long.parseLong(value[1]) + expiredLink;
							if (currentTime > expired) {
								mapResult.put("msg", "Your verification link has expired");
								mapResult.put("status", false);
								return mapResult;
							}
						}
						if (param.contains("email")) {
							SystemUser systemUser = userRepository.findByUsernameIgnoreCase(value[1]);
							if (systemUser != null) {
								systemUser.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
								systemUser.setEnabled(true);
								userRepository.save(systemUser);
								mapResult.put("msg", "Password reset successfully!");
								mapResult.put("status", true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			mapResult.put("msg", "Reset password failed!");
			mapResult.put("status", false);
		}

		return mapResult;
	}

	@Override
	public Map<String, Object> editPassword(UpdateUserDto updateUserDto) {

		Map<String, Object> mapResult = new HashMap<>();

		SystemUser theUser = userRepository.findByUsernameIgnoreCase(updateUserDto.getUsername());
		if (passwordEncoder.matches(updateUserDto.getOldPassword(), theUser.getPassword())) {
			theUser.setPassword(passwordEncoder.encode(updateUserDto.getNewPassword()));
			userRepository.save(theUser);
			mapResult.put("msg", "Password changed successfully!");
			mapResult.put("status", true);
		} else {
			mapResult.put("msg", "Change password failed!");
			mapResult.put("status", false);
		}

		return mapResult;
	}

}

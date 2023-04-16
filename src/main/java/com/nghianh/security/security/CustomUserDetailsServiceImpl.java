package com.nghianh.security.security;

import java.util.HashSet;
import java.util.Set;

import com.nghianh.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nghianh.security.entity.SystemRole;
import com.nghianh.security.entity.SystemUser;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		SystemUser systemUser = userRepository.findByUsernameIgnoreCase(username);
		if (systemUser == null) {
			throw new UsernameNotFoundException("User not found by username: " + username);
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		Set<SystemRole> systemRoles = systemUser.getSystemRoles();

		if (systemRoles != null) {
			for (SystemRole systemRole : systemRoles) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(systemRole.getRoleName());
				grantedAuthorities.add(grantedAuthority);
			}
		}
		// user exist in DB -> create a new user
		// include check account isEnable
		return new UserPrincipal(systemUser.getUsername(), systemUser.getPassword(), systemUser.isEnabled(),
				grantedAuthorities);
	}

}

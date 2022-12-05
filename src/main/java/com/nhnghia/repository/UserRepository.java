package com.nhnghia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnghia.entity.SystemUser;

public interface UserRepository extends JpaRepository<SystemUser, Long> {
	
	boolean findByUsername(String username);

	SystemUser findByUsernameIgnoreCase(String username);

}

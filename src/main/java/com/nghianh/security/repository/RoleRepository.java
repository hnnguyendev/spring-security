package com.nghianh.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nghianh.security.entity.SystemRole;

public interface RoleRepository extends JpaRepository<SystemRole, Long> {
	
	SystemRole findByRoleName(String role);

}

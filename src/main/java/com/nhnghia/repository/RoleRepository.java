package com.nhnghia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnghia.entity.SystemRole;

public interface RoleRepository extends JpaRepository<SystemRole, Long> {
	
	SystemRole findByRoleName(String role);

}

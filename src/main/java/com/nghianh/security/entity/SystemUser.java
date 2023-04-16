package com.nghianh.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nghianh.security.entity.audit.DateAudit;

@Entity
@Table
public class SystemUser extends DateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false, updatable = false, length = 50)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	private String firstname;

	private String lastname;

	@Lob
	private String avatar;

	private boolean enabled;

	// LAZY error, EAGER ok, check -> Group, Permission
	// fixed: add @Transactional -> CustomUserDetailsServiceImpl
	// https://stackoverflow.com/questions/22821695/how-to-fix-hibernate-lazyinitializationexception-failed-to-lazily-initialize-a
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "system_userrole", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", nullable = false, updatable = false) })
	private Set<SystemRole> systemRoles = new HashSet<>();

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "system_usergroup", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "group_id", nullable = false, updatable = false) })
	private Set<SystemGroup> systemGroups = new HashSet<>();

	public SystemUser() {

	}

	public SystemUser(String username, String password, String firstname, String lastname, String avatar,
			boolean enabled, Set<SystemRole> systemRoles) {
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.avatar = avatar;
		this.enabled = enabled;
		this.systemRoles = systemRoles;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<SystemRole> getSystemRoles() {
		return systemRoles;
	}

	public void setSystemRoles(Set<SystemRole> systemRoles) {
		this.systemRoles = systemRoles;
	}

	public Set<SystemGroup> getSystemGroups() {
		return systemGroups;
	}

	public void setSystemGroups(Set<SystemGroup> systemGroups) {
		this.systemGroups = systemGroups;
	}

}

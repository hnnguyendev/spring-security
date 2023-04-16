package com.nghianh.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.nghianh.security.entity.audit.UserDateAudit;

@Entity
@Table
public class SystemGroup extends UserDateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String groupName;

	private String groupCode;

	private String note;

	private boolean enabled;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "system_grouppermission", joinColumns = {
			@JoinColumn(name = "group_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "permission_id", nullable = false, updatable = false) })
	private Set<SystemPermission> systemPermissions = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "system_usergroup", joinColumns = {
			@JoinColumn(name = "group_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", nullable = false, updatable = false) })
	private Set<SystemUser> systemUsers = new HashSet<>();

	public SystemGroup() {

	}

	public Long getId() {
		return id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<SystemPermission> getSystemPermissions() {
		return systemPermissions;
	}

	public void setSystemPermissions(Set<SystemPermission> systemPermissions) {
		this.systemPermissions = systemPermissions;
	}

	public Set<SystemUser> getSystemUsers() {
		return systemUsers;
	}

	public void setSystemUsers(Set<SystemUser> systemUsers) {
		this.systemUsers = systemUsers;
	}

}

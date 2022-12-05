package com.nhnghia.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nhnghia.entity.audit.UserDateAudit;

@Entity
@Table
public class SystemPermission extends UserDateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String permissionName;

	private String permission;

	private Integer sort;

	private Integer parentId;

	private String url;

	private String type;

	private String icon;

	private String htmlId;

	private boolean menuTop;

	private String featureId;

	private String note;

	private boolean enabled;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "system_grouppermission", joinColumns = {
			@JoinColumn(name = "permission_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "group_id", nullable = false, updatable = false) })
	private Set<SystemGroup> systemGroups = new HashSet<>();

	public SystemPermission() {

	}

	public Long getId() {
		return id;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHtmlId() {
		return htmlId;
	}

	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}

	public boolean isMenuTop() {
		return menuTop;
	}

	public void setMenuTop(boolean menuTop) {
		this.menuTop = menuTop;
	}

	public String getFeatureId() {
		return featureId;
	}

	public void setFeatureId(String featureId) {
		this.featureId = featureId;
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

	public Set<SystemGroup> getSystemGroups() {
		return systemGroups;
	}

	public void setSystemGroups(Set<SystemGroup> systemGroups) {
		this.systemGroups = systemGroups;
	}

}

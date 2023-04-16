package com.nghianh.security.service;

import java.util.Map;

import com.nghianh.security.entity.SystemUser;

public interface UserService {

	Map<String, Object> save(SystemUser systemUser);

}

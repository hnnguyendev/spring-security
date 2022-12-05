package com.nhnghia.service;

import java.util.Map;

import com.nhnghia.entity.SystemUser;

public interface UserService {

	Map<String, Object> save(SystemUser systemUser);

}

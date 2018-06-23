package com.software.service;

import java.util.List;

import com.software.pojo.User;

public interface UserService {
	List<User> list();
	List<User> validate(User user);
	int add(User user); 
	int update(User user);
	List<User> emailExist(User user);
}

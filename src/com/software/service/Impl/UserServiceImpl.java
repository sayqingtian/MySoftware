package com.software.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.software.mapper.UserMapper;
import com.software.pojo.User;
import com.software.service.UserService;

@Service
public class UserServiceImpl  implements UserService{
	@Autowired
	UserMapper userMapper;
	
	public List<User> list(){
		return userMapper.list();
	};
	
	public List<User> validate(User user) {
		return userMapper.validate(user);
	}

	@Override
	public int add(User user) {
		// TODO Auto-generated method stub
		return userMapper.add(user);
	}

	@Override
	public int update(User user) {
		// TODO Auto-generated method stub
		return userMapper.update(user);
	}

	@Override
	public List<User> emailExist(User user) {
		// TODO Auto-generated method stub
		return userMapper.emailExist(user);
	}
	
	
}


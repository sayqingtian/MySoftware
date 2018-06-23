package com.software.mapper;

import java.util.List;

import com.software.pojo.User;

public interface UserMapper {
	
	public int add(User user);  
	
	public void delete(int id);  
	
	public User get(int id);  
	
	public int update(User user);   
	
	public List<User> list();
	
	public int count();  
	
	public List<User> validate(User user);
	
	public List<User> emailExist(User user);
}

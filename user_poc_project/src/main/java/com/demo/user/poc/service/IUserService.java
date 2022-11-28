package com.demo.user.poc.service;

import java.util.List;
import java.util.Optional;

import com.demo.user.poc.entity.User;
import com.demo.user.poc.entity.UserMaster;

public interface IUserService {
	
	UserMaster saveUserData(UserMaster userMaster);
	Optional<User> findByUsername(String username);

	public List<UserMaster> findAllUserData();

	public UserMaster findUserById(int userMasterId);

	public List<UserMaster> searchUserMaster(String query);

	public UserMaster findUserByDepartment(String department);

}

package com.demo.user.poc.service;

import com.demo.user.poc.entity.User;
import com.demo.user.poc.entity.UserMaster;
import com.demo.user.poc.repo.UserMasterRepository;
import com.demo.user.poc.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;


	
	@Override
	public UserMaster saveUserData(UserMaster userMaster) {

		


		userMaster.getUser().setPassword(

				pwdEncoder.encode(
						userMaster.getUser().getPassword())

		);

		return userMasterRepository.save(userMaster);
	}


	@Override
	public List<UserMaster> findAllUserData() {
		return userMasterRepository.findAll();
	}



	public UserMaster findUserById(int userMasterId) {
		return userMasterRepository.findById(userMasterId).orElse(null);
	}

	@Override
	public List<UserMaster> searchUserMaster(String query) {
		List<UserMaster> userMasters = userMasterRepository.searchUserMaster(query);
		return userMasters;
	}

	@Override
	public UserMaster findUserByDepartment(String department) {
		return userMasterRepository.findUserByDepartment(department);
	}


	

	//getUser by Username
	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	//-------------------------------------------------------------------//

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.out.println("loadUserByUsername");
		Optional<User> opt =findByUsername(username);
		System.out.println("optopt:"+opt);
		System.out.println("optopt:"+opt.get());
		if (opt.isEmpty())
			throw new UsernameNotFoundException("User not exists!");

		// Read user from DB
		User user = opt.get();
		System.out.println("user.getPassword():"+user.getPassword());
		return new org.springframework.security.core.userdetails.User(
				username,
				user.getPassword(),
				user.getRoles().stream()
						.map(role -> new  SimpleGrantedAuthority(role))
						.collect(Collectors.toList())
				);
	}
}

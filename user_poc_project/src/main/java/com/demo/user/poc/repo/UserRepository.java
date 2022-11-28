package com.demo.user.poc.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.user.poc.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>{

    Optional<User> findByUsername(String username);

}

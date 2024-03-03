package com.springblogapp.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springblogapp.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {
   
	Optional<User> findByEmail(String email);
}

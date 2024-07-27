package com.cognizant.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cognizant.user.model.User;


public interface UserRepository extends MongoRepository<User, String> {
	
	List<User> findByUsernameContaining(String username);
	Optional<User> findByUsername(String username);
	Optional<User> findUserByEmail(String email);

}

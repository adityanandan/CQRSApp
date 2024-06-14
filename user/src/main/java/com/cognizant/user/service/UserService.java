package com.cognizant.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cognizant.user.helper.UserResponse;
import com.cognizant.user.exception.UserException;
import com.cognizant.user.model.User;

public interface UserService {
	
	User createUser(User user);

	User updateUser(User user);

	int deleteUser(User user);

	List<User> getAllUsers();

	List<User> getUserByUsername(String username);

	Optional<User> getUserById(String id);

	UserResponse loginUser(String username, String password) throws UserException;

	Map<String, String> forgotPassword(String username);

	Map<String, String> resetPassword(String username, String password);
}

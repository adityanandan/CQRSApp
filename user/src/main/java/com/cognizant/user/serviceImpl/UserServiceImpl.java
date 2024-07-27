package com.cognizant.user.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.user.exception.ResourceAlreadyPresentException;
import com.cognizant.user.exception.ResourceNotFoundException;
import com.cognizant.user.exception.UserException;
import com.cognizant.user.helper.UserResponse;
import com.cognizant.user.model.User;
import com.cognizant.user.repository.UserRepository;
import com.cognizant.user.service.UserService;
import com.cognizant.user.utils.JwtUtils;

@Service
public class UserServiceImpl implements UserService {

	public static final String SUCCESS = "success";

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtUtils jwtUtils;

	@Override
	public User createUser(User user) {
		user.setJwtToken(jwtUtils.generateJwtToken(user.getUsername()));
		Optional<User> isUserWithEmailPresent = userRepository.findUserByEmail(user.getEmail());
		Optional<User> isUserWithUsernamePresent = userRepository.findByUsername(user.getUsername());
		if (isUserWithUsernamePresent.isPresent()) {
			throw new ResourceAlreadyPresentException("The username " + user.getUsername() + " is already registered");
		} else if (isUserWithEmailPresent.isPresent()) {
			throw new ResourceAlreadyPresentException("The emailId " + user.getEmail() + " is already registered");
		} else {
			return userRepository.save(user);
		}

	}

	@Override
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public int deleteUser(User user) {
		userRepository.delete(user);
		return 1;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<User> getUserByUsername(String username) {
		return userRepository.findByUsernameContaining(username);
	}

	@Override
	public Optional<User> getUserById(String id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {

			throw new ResourceNotFoundException("User not present in Database");
		}

		return user;
	}

	@Override
	public UserResponse loginUser(String username, String password) throws UserException {
		UserResponse response = new UserResponse();
		Optional<User> loginUser = userRepository.findByUsername(username);
		if (!loginUser.isPresent()) {
			throw new ResourceNotFoundException("No user with this name");
		}
		User user = loginUser.get();
		if (user.getPassword().equals(password)) {
			response.setUser(user);
			response.setLoginStatus(SUCCESS);
		} else {
			response.setLoginStatus("failed");
		}
		return response;
	}

	@Override
	public Map<String, String> forgotPassword(String username) {
		Map<String, String> map = new HashMap<>();
		Optional<User> loginUser = userRepository.findByUsername(username);
		if (!loginUser.isPresent()) {
			throw new ResourceNotFoundException("Give correct username");
		}
		User user = loginUser.get();
		user.setPassword(UUID.randomUUID().toString());
		userRepository.save(user);
		map.put("newPassword", user.getPassword());
		map.put("resetStatus", SUCCESS);
		return map;
	}

	@Override
	public Map<String, String> resetPassword(String username, String password) {
		Map<String, String> map = new HashMap<>();
		Optional<User> loginUser = userRepository.findByUsername(username);
		if (!loginUser.isPresent()) {
			throw new ResourceNotFoundException("Give correct username");
		}
		User user = loginUser.get();
		user.setPassword(password);
		userRepository.save(user);
		map.put("newPassword", user.getPassword());
		map.put("resetStatus", SUCCESS);
		return map;
	}

	@Override
	public String getJwtTokenByUsername(String username) {
		Optional<User> isUserWithUsernamePresent = userRepository.findByUsername(username);
		if (!isUserWithUsernamePresent.isPresent()) {
			throw new ResourceNotFoundException("The username " + username + " is not registered");
		}
		return isUserWithUsernamePresent.get().getJwtToken();
	}

}
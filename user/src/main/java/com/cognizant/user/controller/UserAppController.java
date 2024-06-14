package com.cognizant.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.user.exception.ResourceAlreadyPresentException;
import com.cognizant.user.helper.UserResponse;
import com.cognizant.user.model.User;
import com.cognizant.user.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1.0/user")
public class UserAppController {

	@Autowired
	UserService userService;
	
	Logger logger = LoggerFactory.getLogger(UserAppController.class);

	@PostMapping("/user/register")
	public ResponseEntity<Object> registerUser(@RequestBody User user) {
		try {
			User createUser = userService.createUser(user);
			return new ResponseEntity<>(createUser, HttpStatus.CREATED);
		} catch (ResourceAlreadyPresentException message) {
			return new ResponseEntity<>(message.getMessage(), HttpStatus.CONFLICT);

		}
	}

	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody User user) {

		try {

			return new ResponseEntity<>(userService.loginUser(user.getUsername(), user.getPassword()), HttpStatus.OK);

		} catch (Exception e) {
			UserResponse userResponse = new UserResponse();
			userResponse.setLoginStatus("username does not exist");
			logger.info("User {} does not exist", user.getUsername());
			return new ResponseEntity<>(userResponse, HttpStatus.OK);
		}
	}

	@ResponseBody
	@GetMapping("/{username}/forgot")
	public Map<String, String> forgotPassword(@PathVariable("username") String username) {
		return new HashMap<>(userService.forgotPassword(username));

	}

	@ResponseBody
	@PostMapping("/reset")
	public Map<String, String> resetUserPassword(@RequestBody User user) {
		return new HashMap<>(userService.resetPassword(user.getUsername(), user.getPassword()));
	}


	@GetMapping("/users/all")
	public ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@GetMapping("/user/search/{username}")
	public ResponseEntity<List<User>> searchUser(@PathVariable("username") String username) {
		return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
	}



}

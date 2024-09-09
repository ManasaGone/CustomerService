package com.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.user.dtos.ChangePasswordDto;
import com.user.dtos.LoginRequest;
import com.user.entities.Customer;
import com.user.repositories.CustomerRepository;
import com.user.response.JwtResponse;
import com.user.response.MessageResponse;
import com.user.security.jwt.JwtUtils;
import com.user.security.services.UserDetailsImpl;
import com.user.security.services.UserDetailsServiceImpl;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/customer/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getCustomerId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail()
									                
												 ));
	}
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody Customer signUpRequest) {
		if (customerRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		if (customerRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		// Create new user's account
		Customer customer = new Customer(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getGender(),
                signUpRequest.getEmail(),
                signUpRequest.getPhNumber(),     
                signUpRequest.getAddress(),
                signUpRequest.getDob()
							 );

		customerRepository.save(customer);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	 @PostMapping("/changePassword")
	    public String changePassword(@Valid @RequestBody ChangePasswordDto changepasswordDto) {
	        
			return userDetailsServiceImpl.changePassword(changepasswordDto.getUsername(), changepasswordDto.getOldPassword(), changepasswordDto.getNewPassword());
	    }

}


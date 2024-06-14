package com.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.security.JwtResponse;
import com.learn.security.Payload;
import com.learn.security.service.CustomUserDetails;
import com.learn.security.util.JwtHelper;

@RestController
public class LoginController {
	
	@Autowired
	private CustomUserDetails customUserDetails;
	
	@Autowired
	private JwtHelper helper;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/token")
	public ResponseEntity<?> generateToken(@RequestBody Payload payload){
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getUserName(), payload.getPassword()));
		}catch(UsernameNotFoundException e){
			e.printStackTrace();
			throw new BadCredentialsException("Your Credentials are not valid!!");
		}
		UserDetails userDetails = this.customUserDetails.loadUserByUsername(payload.getUserName());
		String token = this.helper.generateToken(userDetails);
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setToken(token);
		return ResponseEntity.ok(jwtResponse);
	}
}

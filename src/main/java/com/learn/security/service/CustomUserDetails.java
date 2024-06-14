package com.learn.security.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learn.entity.Employee;
import com.learn.repository.EmployeeRespository;

@Service
public class CustomUserDetails implements UserDetailsService {

	@Autowired
	private EmployeeRespository employeeRespository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRespository.findByName(username);
		if(employee==null)
			throw new UsernameNotFoundException("User Not Found with UserName :"+username);
	return new User(employee.getName(),employee.getPassword(),new ArrayList<>());
	}

}

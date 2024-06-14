package com.learn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learn.entity.Employee;

public interface EmployeeRespository extends JpaRepository<Employee, Long>{

	Employee findByName(String username);

}

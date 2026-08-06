package com.jhoel.empleados_api.repository;

import com.jhoel.empleados_api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);
}

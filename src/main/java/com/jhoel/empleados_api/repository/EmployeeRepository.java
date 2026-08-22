package com.jhoel.empleados_api.repository;

import com.jhoel.empleados_api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Page<Employee> findByFirstNameContainingIgnoreCase(String firstName, Pageable pageable);

    @Query("""
        SELECT
            COUNT(e) AS totalEmployees,
            COALESCE(AVG(e.salary), 0) AS averageSalary,
            COALESCE(MAX(e.salary), 0) AS maxSalary
        FROM Employee e
        """)
    EmployeeStatisticsProjection getEmployeeStatistics();
}

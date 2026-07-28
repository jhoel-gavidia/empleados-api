package com.jhoel.empleados_api.repository;

import com.jhoel.empleados_api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

package com.jhoel.empleados_api.repository;

import com.jhoel.empleados_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

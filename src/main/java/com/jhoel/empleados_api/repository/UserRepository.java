package com.jhoel.empleados_api.repository;

import com.jhoel.empleados_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    Select u
    from User u
    Where u.username = :identifier
    Or u.email = :identifier
""")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);
}

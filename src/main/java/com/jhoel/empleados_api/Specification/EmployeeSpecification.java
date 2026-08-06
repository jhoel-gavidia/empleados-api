package com.jhoel.empleados_api.Specification;

import com.jhoel.empleados_api.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Employee> hasDepartment(Long id) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("department").get("id"), id);
    }

    public static Specification<Employee> salaryGreaterThan(BigDecimal salary) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("salary"), salary);
    }

    public static Specification<Employee> salaryLessThan(BigDecimal salary) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("salary"), salary);
    }
}

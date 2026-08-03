package com.jhoel.empleados_api.mapper;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.entity.Employee;

public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest employeeRequest) {

        return Employee.builder()
                .firstName(employeeRequest.getFirstName())
                .lastName(employeeRequest.getLastName())
                .email(employeeRequest.getEmail())
                .birthDate(employeeRequest.getBirthDate())
                .phoneNumber(employeeRequest.getPhoneNumber())
                .salary(employeeRequest.getSalary()).build();
    }

    public EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .birthDate(employee.getBirthDate())
                .phoneNumber(employee.getPhoneNumber())
                .salary(employee.getSalary())
                .departmentId(employee.getDepartment().getId())
                .departmentName(employee.getDepartment().getName())
                .build();
    }
}

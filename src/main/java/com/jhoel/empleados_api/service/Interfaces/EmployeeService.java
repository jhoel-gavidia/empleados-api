package com.jhoel.empleados_api.service.Interfaces;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.entity.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
    EmployeeResponse getEmployeeById(Long id);
    List<EmployeeResponse> getEmployees();
}

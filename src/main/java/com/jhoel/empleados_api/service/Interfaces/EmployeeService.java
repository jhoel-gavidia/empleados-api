package com.jhoel.empleados_api.service.Interfaces;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
    EmployeeResponse getEmployeeById(Long id);
    Page<EmployeeResponse> getEmployees(String name, Pageable pageable);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest);
    void deleteEmployee(Long id);
}

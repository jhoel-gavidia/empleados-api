package com.jhoel.empleados_api.service.Interfaces;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
}

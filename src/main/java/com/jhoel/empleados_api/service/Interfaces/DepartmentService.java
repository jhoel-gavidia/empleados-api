package com.jhoel.empleados_api.service.Interfaces;

import com.jhoel.empleados_api.dto.request.DepartmentRequest;
import com.jhoel.empleados_api.dto.response.DepartmentResponse;
import com.jhoel.empleados_api.entity.Department;

public interface DepartmentService {
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest);
}

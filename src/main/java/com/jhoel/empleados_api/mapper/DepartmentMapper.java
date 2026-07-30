package com.jhoel.empleados_api.mapper;


import com.jhoel.empleados_api.dto.request.DepartmentRequest;
import com.jhoel.empleados_api.dto.response.DepartmentResponse;
import com.jhoel.empleados_api.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequest departmentRequest) {
        return Department.builder()
                .name(departmentRequest.getName())
                .officeLocation(departmentRequest.getOfficeLocation())
                .build();
    }

    public DepartmentResponse toResponse(Department department) {
        return  DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .officeLocation(department.getOfficeLocation())
                .build();
    }

}

package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.dto.request.DepartmentRequest;
import com.jhoel.empleados_api.dto.response.DepartmentResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.exception.AlreadyExistsException;
import com.jhoel.empleados_api.exception.NotFoundException;
import com.jhoel.empleados_api.mapper.DepartmentMapper;
import com.jhoel.empleados_api.repository.DepartmentRepository;
import com.jhoel.empleados_api.service.Interfaces.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DepartmentImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        boolean exists = departmentRepository.existsByName(departmentRequest.getName());

        if(exists){
            throw new AlreadyExistsException("Department with name " + departmentRequest.getName() + " already exists");
        }

        Department department = departmentMapper.toEntity(departmentRequest);

        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.toResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department  Not Found with id " + id)
        );

        return departmentMapper.toResponse(department);
    }

}

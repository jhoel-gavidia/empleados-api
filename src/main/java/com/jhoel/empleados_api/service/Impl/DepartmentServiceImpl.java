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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department  Not Found with id " + id)
        );

        return departmentMapper.toResponse(department);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public List<DepartmentResponse> getDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department  Not Found with id " + id)
        );

        boolean exists = departmentRepository
                .existsByNameAndIdNot(departmentRequest.getName(), id);

        if (exists) {
            throw new AlreadyExistsException(
                    "Department with name " + departmentRequest.getName() + " already exists"
            );
        }

        department.setName(departmentRequest.getName());
        department.setOfficeLocation(departmentRequest.getOfficeLocation());


        Department savedDepartment = departmentRepository.save(department);

        return departmentMapper.toResponse(savedDepartment);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Department Not Found with id " + id)
        );

        departmentRepository.delete(department);
    }
}

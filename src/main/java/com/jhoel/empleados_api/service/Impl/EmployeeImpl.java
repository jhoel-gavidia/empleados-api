package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.entity.Employee;
import com.jhoel.empleados_api.exception.NotFoundException;
import com.jhoel.empleados_api.mapper.EmployeeMapper;
import com.jhoel.empleados_api.repository.DepartmentRepository;
import com.jhoel.empleados_api.repository.EmployeeRepository;
import com.jhoel.empleados_api.service.Interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Department department = departmentRepository.findById(employeeRequest.getDepartmentId()).orElseThrow(
                () -> new NotFoundException("Department not found with id:" + employeeRequest.getDepartmentId())
        );

        Employee employee = employeeMapper.toEntity(employeeRequest);
        employee.setDepartment(department);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee not found with id:" + id)
        );

        return employeeMapper.toResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee not found with id:" + id)
        );

        Department department = departmentRepository.findById(employeeRequest.getDepartmentId()).orElseThrow(
                () -> new NotFoundException("Department not found with id:" + employeeRequest.getDepartmentId())
        );

        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setBirthDate(employeeRequest.getBirthDate());
        employee.setPhoneNumber(employeeRequest.getPhoneNumber());
        employee.setSalary(employeeRequest.getSalary());
        employee.setDepartment(department);

        employeeRepository.save(employee);

        return employeeMapper.toResponse(employee);
    }

}

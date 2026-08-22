package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.Specification.EmployeeSpecification;
import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.dto.response.EmployeeStatisticsResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.entity.Employee;
import com.jhoel.empleados_api.exception.NotFoundException;
import com.jhoel.empleados_api.mapper.EmployeeMapper;
import com.jhoel.empleados_api.repository.DepartmentRepository;
import com.jhoel.empleados_api.repository.EmployeeRepository;
import com.jhoel.empleados_api.repository.EmployeeStatisticsProjection;
import com.jhoel.empleados_api.service.Interfaces.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee not found with id:" + id)
        );

        return employeeMapper.toResponse(employee);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public Page<EmployeeResponse> getEmployees(String name, Pageable pageable) {
        Page<Employee> employees;

        if(name == null || name.isBlank()) {
            employees = employeeRepository.findAll(pageable);

        } else {
            employees = employeeRepository.findByFirstNameContainingIgnoreCase(name, pageable);
        }

        return employees.map(employeeMapper::toResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Employee not found with id:" + id)
        );

        employeeRepository.delete(employee);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public Page<EmployeeResponse> filterEmployee(String name,
                                                 Long departmentId,
                                                 BigDecimal maxSalary,
                                                 BigDecimal minSalary,
                                                 Pageable pageable) {

        Specification<Employee> spec = buildEmployeeSpecification(name, departmentId, maxSalary, minSalary);

        return  employeeRepository.findAll(spec, pageable)
                .map(employeeMapper::toResponse);

    }

    private Specification<Employee> buildEmployeeSpecification(String name,
                                                  Long departmentId,
                                                  BigDecimal maxSalary,
                                                  BigDecimal minSalary) {

        Specification<Employee> spec = Specification.unrestricted();

        if(name != null && !name.isBlank()) {
            spec = spec.and(EmployeeSpecification.hasName(name));
        }

        if(departmentId != null) {
            spec = spec.and((EmployeeSpecification.hasDepartment(departmentId)));
        }

        if(maxSalary != null) {
            spec = spec.and(EmployeeSpecification.salaryLessThan(maxSalary));
        }

        if(minSalary != null) {
            spec = spec.and(EmployeeSpecification.salaryGreaterThan(minSalary));
        }



        return spec;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Override
    public EmployeeStatisticsResponse getEmployeeStatistics() {

        EmployeeStatisticsProjection statistics =
                employeeRepository.getEmployeeStatistics();

        long totalEmployees = statistics.getTotalEmployees();

        BigDecimal averageSalary = statistics.getAverageSalary();

        BigDecimal maxSalary = statistics.getMaxSalary();

        long totalDepartments = departmentRepository.count();

        return new EmployeeStatisticsResponse(
                totalEmployees,
                totalDepartments,
                averageSalary,
                maxSalary
        );
    }
}

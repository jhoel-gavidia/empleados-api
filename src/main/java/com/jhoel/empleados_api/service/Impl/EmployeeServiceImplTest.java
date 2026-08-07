package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.entity.Employee;
import com.jhoel.empleados_api.exception.NotFoundException;
import com.jhoel.empleados_api.mapper.EmployeeMapper;
import com.jhoel.empleados_api.repository.DepartmentRepository;
import com.jhoel.empleados_api.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;


    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @Test
    void shouldCreateEmployeeSuccessfully() {
        EmployeeRequest employeeRequest = new EmployeeRequest();

        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("jhoelgavidia@gmail.com");
        employeeRequest.setBirthDate(LocalDate.of(2000, 1, 10));
        employeeRequest.setPhoneNumber("999999999");
        employeeRequest.setSalary(new BigDecimal("3500"));
        employeeRequest.setDepartmentId(1L);

        Department department = Department.builder()
                .id(1L)
                .name("Jhoelito")
                .officeLocation("Lima")
                .build();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 10))
                .phoneNumber("999999999")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeMapper.toEntity(employeeRequest))
                .thenReturn(employee);
        when(employeeRepository.save(employee))
                .thenReturn(employee);

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 10))
                .phoneNumber("999999999")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeServiceImpl.createEmployee(employeeRequest);

        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jhoelgavidia@gmail.com", result.getEmail());

        verify(departmentRepository).findById(1L);
        verify(employeeMapper).toEntity(employeeRequest);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void shouldDepartmentDoesNotExist() {

        EmployeeRequest employeeRequest = new EmployeeRequest();

        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("jhoelgavidia@gmail.com");
        employeeRequest.setBirthDate(LocalDate.of(2000, 1, 10));
        employeeRequest.setPhoneNumber("999999999");
        employeeRequest.setSalary(new BigDecimal("3500"));
        employeeRequest.setDepartmentId(99L);

        when(departmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> employeeServiceImpl.createEmployee(employeeRequest)
        );

        verify(employeeRepository, never()).save(any(Employee.class));
    }
}

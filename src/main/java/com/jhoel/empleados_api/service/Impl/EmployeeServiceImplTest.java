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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    @Test
    void shouldGetEmployeeByIdisSuccessfully() {
        Long id = 1L;

        Employee employee = Employee.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 10))
                .phoneNumber("999999999")
                .salary(new BigDecimal("3500"))
                .build();

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 10))
                .phoneNumber("999999999")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result = employeeServiceImpl.getEmployeeById(id);

        assertEquals(id, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("jhoelgavidia@gmail.com", result.getEmail());


        verify(employeeRepository).findById(id);
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void shouldEmployyeDoesNotExists() {
        Long id = 1L;

        when(employeeRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> employeeServiceImpl.getEmployeeById(id)
        );

        verify(employeeRepository).findById(id);
        verify(employeeMapper, never()).toResponse(any(Employee.class));
    }

    @Test
    void shouldGetEmployeeWithoutFilter() {

        Pageable pageable = PageRequest.of(0, 10);

        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee), pageable, 1);

        when(employeeRepository.findAll(pageable))
                .thenReturn(employeePage);

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        Page<EmployeeResponse> result =
                employeeServiceImpl.getEmployees(null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("Doe", result.getContent().get(0).getLastName());

        verify(employeeRepository).findAll(pageable);
        verify(employeeRepository, never())
                .findByFirstNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void shouldGetEmployeeByName() {
        String name = "John";
        Pageable pageable = PageRequest.of(0, 10);

        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee), pageable, 1);

        when(employeeRepository.findByFirstNameContainingIgnoreCase(
                name, pageable
        )).thenReturn(employeePage);

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("jhoelgavidia@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);


        Page<EmployeeResponse> result =
                employeeServiceImpl.getEmployees(name, pageable);


        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("Doe", result.getContent().get(0).getLastName());


        verify(employeeRepository)
                .findByFirstNameContainingIgnoreCase(name, pageable);

        verify(employeeRepository, never())
                .findAll(pageable);
    }

    @Test
    void shouldUpdateEmployeeSuccessully() {

        Long employeeId = 1L;

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John Updated");
        employeeRequest.setLastName("Doe Updated");
        employeeRequest.setEmail("john.updated@gmail.com");
        employeeRequest.setBirthDate(LocalDate.of(2000, 1, 10));
        employeeRequest.setPhoneNumber("999999999");
        employeeRequest.setSalary(new BigDecimal("4000"));
        employeeRequest.setDepartmentId(2L);

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 10))
                .phoneNumber("988888888")
                .salary(new BigDecimal("3500"))
                .build();

        Department department = Department.builder()
                .id(2L)
                .name("Recursos Humanos")
                .officeLocation("Lima")
                .build();

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        when(departmentRepository.findById(2L))
                .thenReturn(Optional.of(department));

        when(employeeRepository.save(employee))
                .thenReturn(employee);

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(employeeId)
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("john.updated@gmail.com")
                .birthDate(LocalDate.of(2000, 1, 10))
                .phoneNumber("999999999")
                .salary(new BigDecimal("4000"))
                .build();

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        EmployeeResponse result =
                employeeServiceImpl.updateEmployee(employeeId, employeeRequest);

        assertEquals("John Updated", employee.getFirstName());
        assertEquals("Doe Updated", employee.getLastName());
        assertEquals("john.updated@gmail.com", employee.getEmail());
        assertEquals(new BigDecimal("4000"), employee.getSalary());
        assertEquals(department, employee.getDepartment());

        verify(employeeRepository).findById(employeeId);
        verify(departmentRepository).findById(2L);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExist() {


        Long employeeId = 99L;

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john@gmail.com");
        employeeRequest.setDepartmentId(1L);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> employeeServiceImpl.updateEmployee(
                        employeeId,
                        employeeRequest
                )
        );

        verify(employeeRepository).findById(employeeId);
        verify(departmentRepository, never()).findById(anyLong());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldThrowExceptionWhenDepartmentDoesNotExist() {

        Long employeeId = 1L;

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John Updated");
        employeeRequest.setLastName("Doe Updated");
        employeeRequest.setEmail("john.updated@gmail.com");
        employeeRequest.setBirthDate(LocalDate.of(2000, 1, 10));
        employeeRequest.setPhoneNumber("999999999");
        employeeRequest.setSalary(new BigDecimal("4000"));
        employeeRequest.setDepartmentId(99L);

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> employeeServiceImpl.updateEmployee(
                        employeeId,
                        employeeRequest
                )
        );

        verify(employeeRepository).findById(employeeId);
        verify(departmentRepository).findById(99L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldDeleteEmployeeSuccessfully() {

        Long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .build();

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));


        employeeServiceImpl.deleteEmployee(employeeId);


        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeDoesNotExistOnDelete() {

        Long employeeId = 99L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());


        assertThrows(
                NotFoundException.class,
                () -> employeeServiceImpl.deleteEmployee(employeeId)
        );

        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    void shouldFilterEmployeesSuccessfully() {

        String name = "John";
        Long departmentId = 1L;
        BigDecimal minSalary = new BigDecimal("3000");
        BigDecimal maxSalary = new BigDecimal("5000");

        Pageable pageable = PageRequest.of(0, 10);

        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee), pageable, 1);

        when(employeeRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(employeePage);

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .salary(new BigDecimal("3500"))
                .build();

        when(employeeMapper.toResponse(employee))
                .thenReturn(employeeResponse);

        Page<EmployeeResponse> result =
                employeeServiceImpl.filterEmployee(
                        name,
                        departmentId,
                        maxSalary,
                        minSalary,
                        pageable
                );

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("Doe", result.getContent().get(0).getLastName());
        assertEquals(new BigDecimal("3500"), result.getContent().get(0).getSalary());

        verify(employeeRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
        verify(employeeMapper).toResponse(employee);
    }
}

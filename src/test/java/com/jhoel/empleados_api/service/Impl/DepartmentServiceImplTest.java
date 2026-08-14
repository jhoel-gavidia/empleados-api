package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.dto.request.DepartmentRequest;
import com.jhoel.empleados_api.dto.response.DepartmentResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.exception.AlreadyExistsException;
import com.jhoel.empleados_api.exception.NotFoundException;
import com.jhoel.empleados_api.mapper.DepartmentMapper;
import com.jhoel.empleados_api.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;


    @Test
    void createDepartment_shouldCreateSuccessfully() {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("Sistemas");
        request.setOfficeLocation("Lima");

        Department department = new Department();
        Department savedDepartment = new Department();

        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Sistemas")
                .officeLocation("Lima")
                .build();

        when(departmentRepository.existsByName("Sistemas"))
                .thenReturn(false);

        when(departmentMapper.toEntity(request))
                .thenReturn(department);

        when(departmentRepository.save(department))
                .thenReturn(savedDepartment);

        when(departmentMapper.toResponse(savedDepartment))
                .thenReturn(response);

        DepartmentResponse result =
                departmentService.createDepartment(request);

        assertEquals("Sistemas", result.getName());
        assertEquals("Lima", result.getOfficeLocation());

        verify(departmentRepository).save(department);
    }


    @Test
    void createDepartment_shouldThrowWhenNameAlreadyExists() {

        DepartmentRequest request = new DepartmentRequest();
        request.setName("Sistemas");
        request.setOfficeLocation("Lima");

        when(departmentRepository.existsByName("Sistemas"))
                .thenReturn(true);

        assertThrows(
                AlreadyExistsException.class,
                () -> departmentService.createDepartment(request)
        );

        verify(departmentRepository, never()).save(any());
    }


    @Test
    void getDepartmentById_shouldReturnDepartment() {

        Department department = new Department();

        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Sistemas")
                .officeLocation("Lima")
                .build();

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentMapper.toResponse(department))
                .thenReturn(response);

        DepartmentResponse result =
                departmentService.getDepartmentById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Sistemas", result.getName());
    }


    @Test
    void getDepartmentById_shouldThrowWhenNotFound() {

        when(departmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> departmentService.getDepartmentById(999L)
        );
    }


    @Test
    void getDepartments_shouldReturnDepartments() {

        Department department = new Department();

        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Sistemas")
                .officeLocation("Lima")
                .build();

        when(departmentRepository.findAll())
                .thenReturn(List.of(department));

        when(departmentMapper.toResponse(department))
                .thenReturn(response);

        List<DepartmentResponse> result =
                departmentService.getDepartments();

        assertEquals(1, result.size());
        assertEquals("Sistemas", result.get(0).getName());
    }


    @Test
    void updateDepartment_shouldUpdateSuccessfully() {

        Department department = new Department();

        DepartmentRequest request = new DepartmentRequest();
        request.setName("Marketing");
        request.setOfficeLocation("Huacho");

        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Marketing")
                .officeLocation("Huacho")
                .build();

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.existsByNameAndIdNot("Marketing", 1L))
                .thenReturn(false);

        when(departmentRepository.save(department))
                .thenReturn(department);

        when(departmentMapper.toResponse(department))
                .thenReturn(response);

        DepartmentResponse result =
                departmentService.updateDepartment(1L, request);

        assertEquals("Marketing", result.getName());
        assertEquals("Huacho", result.getOfficeLocation());

        verify(departmentRepository).save(department);
    }


    @Test
    void updateDepartment_shouldThrowWhenNameAlreadyExists() {

        Department department = new Department();

        DepartmentRequest request = new DepartmentRequest();
        request.setName("Sistemas");
        request.setOfficeLocation("Lima");

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        when(departmentRepository.existsByNameAndIdNot("Sistemas", 1L))
                .thenReturn(true);

        assertThrows(
                AlreadyExistsException.class,
                () -> departmentService.updateDepartment(1L, request)
        );

        verify(departmentRepository, never()).save(any());
    }


    @Test
    void deleteDepartment_shouldDeleteSuccessfully() {

        Department department = new Department();

        when(departmentRepository.findById(1L))
                .thenReturn(Optional.of(department));

        departmentService.deleteDepartment(1L);

        verify(departmentRepository).delete(department);
    }


    @Test
    void deleteDepartment_shouldThrowWhenNotFound() {

        when(departmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> departmentService.deleteDepartment(999L)
        );

        verify(departmentRepository, never()).delete(any());
    }
}
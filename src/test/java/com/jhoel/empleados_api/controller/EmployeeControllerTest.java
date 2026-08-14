package com.jhoel.empleados_api.controller;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.security.CustomUserDetailsService;
import com.jhoel.empleados_api.security.JwtService;
import com.jhoel.empleados_api.service.Interfaces.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(EmployeeController.class)
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmployeeService employeeService;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;


    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_201() throws Exception {

        EmployeeResponse response = mock(EmployeeResponse.class);

        when(response.getId()).thenReturn(1L);

        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Juan",
                            "lastName": "Perez",
                            "email": "juan@email.com",
                            "birthDate": "2000-01-01",
                            "phoneNumber": "987654321",
                            "salary": 2500,
                            "departmentId": 1
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/api/employees/1"
                ));

        verify(employeeService).createEmployee(any(EmployeeRequest.class));
    }


    @Test
    @WithMockUser
    void getEmployeeById_200() throws Exception {

        EmployeeResponse response = mock(EmployeeResponse.class);

        when(employeeService.getEmployeeById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk());

        verify(employeeService).getEmployeeById(1L);
    }


    @Test
    @WithMockUser
    void getEmployees_200() throws Exception {

        EmployeeResponse response = mock(EmployeeResponse.class);

        Page<EmployeeResponse> page =
                new PageImpl<>(List.of(response));

        when(employeeService.getEmployees(
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk());

        verify(employeeService).getEmployees(
                any(),
                any()
        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployee_200() throws Exception {

        EmployeeResponse response = mock(EmployeeResponse.class);

        when(employeeService.updateEmployee(
                eq(1L),
                any(EmployeeRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/employees/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "Juan",
                            "lastName": "Perez",
                            "email": "juan@email.com",
                            "birthDate": "2000-01-01",
                            "phoneNumber": "987654321",
                            "salary": 3000,
                            "departmentId": 1
                        }
                        """))
                .andExpect(status().isOk());

        verify(employeeService).updateEmployee(
                eq(1L),
                any(EmployeeRequest.class)
        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_204() throws Exception {

        doNothing()
                .when(employeeService)
                .deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(1L);
    }


    @Test
    @WithMockUser
    void filterEmployee_200() throws Exception {

        EmployeeResponse response = mock(EmployeeResponse.class);

        Page<EmployeeResponse> page =
                new PageImpl<>(List.of(response));

        when(employeeService.filterEmployee(
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/api/employees/filter")
                        .param("name", "Juan")
                        .param("departmentId", "1")
                        .param("maxSalary", "5000")
                        .param("minSalary", "1000"))
                .andExpect(status().isOk());

        verify(employeeService).filterEmployee(
                any(),
                any(),
                any(),
                any(),
                any()
        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_shouldReturn400WhenRequestIsInvalid() throws Exception {

        mockMvc.perform(post("/api/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "firstName": "",
                            "lastName": "",
                            "email": "invalid-email",
                            "birthDate": "2030-01-01",
                            "phoneNumber": "123",
                            "salary": -100,
                            "departmentId": 0
                        }
                        """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }


    @Test
    void getEmployeeById_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(employeeService);
    }
}
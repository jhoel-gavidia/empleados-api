package com.jhoel.empleados_api.controller;

import com.jhoel.empleados_api.dto.request.DepartmentRequest;
import com.jhoel.empleados_api.dto.response.DepartmentResponse;
import com.jhoel.empleados_api.exception.NotFoundException;
import com.jhoel.empleados_api.security.CustomUserDetailsService;
import com.jhoel.empleados_api.security.JwtService;
import com.jhoel.empleados_api.service.Interfaces.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@ActiveProfiles("test")
public class DepartmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    DepartmentService departmentService;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser
    void getDepartmentById_200() throws Exception {
        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk());

        DepartmentResponse departmentResponse = DepartmentResponse.builder()
                .id(1L)
                .name("Sistemas")
                .officeLocation("Lima")
                .build();

        when(departmentService.getDepartmentById(1L))
                .thenReturn(departmentResponse);

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sistemas"))
                .andExpect(jsonPath("$.officeLocation").value("Lima"));
    }

    @Test
    @WithMockUser
    void getDepartmentById_404() throws Exception {
        when(departmentService.getDepartmentById(99L))
                .thenThrow(new NotFoundException("Department not found"));

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDepartment_201() throws Exception {
        DepartmentResponse response = DepartmentResponse.builder()
                .id(1L)
                .name("Sistemas")
                .officeLocation("Lima")
                .build();

        when(departmentService.createDepartment(any(DepartmentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Sistemas",
                                "officeLocation": "Lima"
                            }
                            """))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDepartment_400() throws Exception {
        mockMvc.perform(post("/api/departments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "",
                                "officeLocation": "Lima"
                            }
                            """))
                .andExpect(status().isBadRequest());
    }
}

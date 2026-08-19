package com.jhoel.empleados_api.controller;

import com.jhoel.empleados_api.dto.request.DepartmentRequest;
import com.jhoel.empleados_api.dto.response.DepartmentResponse;
import com.jhoel.empleados_api.entity.Department;
import com.jhoel.empleados_api.service.Interfaces.DepartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(
            @Valid @RequestBody DepartmentRequest departmentRequest
    ) {

        DepartmentResponse response = departmentService.createDepartment(departmentRequest);

        URI location =  URI.create("/api/departments/" + response.getId());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id){
        DepartmentResponse response = departmentService.getDepartmentById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> response = departmentService.getDepartments();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest departmentRequest
    ){
        DepartmentResponse response = departmentService.updateDepartment(id, departmentRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);

        return ResponseEntity.noContent().build();
    }
}

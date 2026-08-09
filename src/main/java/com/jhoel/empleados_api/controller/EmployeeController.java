package com.jhoel.empleados_api.controller;

import com.jhoel.empleados_api.dto.request.EmployeeRequest;
import com.jhoel.empleados_api.dto.response.EmployeeResponse;
import com.jhoel.empleados_api.service.Interfaces.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest employeeRequest) {

        EmployeeResponse response = employeeService.createEmployee(employeeRequest);

        URI location = URI.create("/api/employees/" + response.getId());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getEmployees(@RequestParam(required = false) String name, Pageable pageable) {
        return ResponseEntity.ok(employeeService.getEmployees(name, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<EmployeeResponse>> filterEmployee(@RequestParam(required = false) String name,
                                                                 @RequestParam(required = false) Long departmentId,
                                                                 @RequestParam(required = false) BigDecimal maxSalary,
                                                                 @RequestParam(required = false) BigDecimal minSalary,
                                                                 Pageable pageable)
    {
        return ResponseEntity.ok(employeeService.filterEmployee(name, departmentId, maxSalary, minSalary, pageable));
    }
}

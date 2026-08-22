package com.jhoel.empleados_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class EmployeeStatisticsResponse {

    private long totalEmployees;
    private long totalDepartments;
    private BigDecimal averageSalary;
    private BigDecimal maxSalary;
}
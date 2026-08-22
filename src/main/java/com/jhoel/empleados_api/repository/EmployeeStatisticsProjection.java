package com.jhoel.empleados_api.repository;

import java.math.BigDecimal;

public interface EmployeeStatisticsProjection {

    Long getTotalEmployees();

    BigDecimal getAverageSalary();

    BigDecimal getMaxSalary();
}
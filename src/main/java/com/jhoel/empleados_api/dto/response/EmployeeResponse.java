package com.jhoel.empleados_api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
public class EmployeeResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final LocalDate birthDate;
    private final String phoneNumber;
    private final BigDecimal salary;
    private final Long departmentId;
    private final String departmentName;

}

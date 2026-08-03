package com.jhoel.empleados_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Lastname is required")
    private String lastName;


    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotNull
    @Past(message = "Please provide a valid birth date")
    private LocalDate birthDate;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^9\\d{8}$",
            message = "Please provide a valid phone number")
    private String phoneNumber;

    @NotNull(message = "Salary is requerid")
    @PositiveOrZero(message = "Salary must be zero or greater")
    private BigDecimal salary;

    @NotNull(message = "Department is requerid")
    @Positive(message = "Department id must be greater than zero")
    private Long departmentId;
}

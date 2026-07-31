package com.jhoel.empleados_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {

    private int status;
    private Map<String, String> errors;
}

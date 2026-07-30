package com.jhoel.empleados_api.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class DepartmentResponse {
    private final Long id;
    private final String name;
    private final String officeLocation;
}

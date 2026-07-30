package com.jhoel.empleados_api.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
    private String name;
    private String officeLocation;
}

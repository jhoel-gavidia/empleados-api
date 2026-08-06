package com.jhoel.empleados_api.dto.response;


import com.jhoel.empleados_api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String username;
    private Role role;

}

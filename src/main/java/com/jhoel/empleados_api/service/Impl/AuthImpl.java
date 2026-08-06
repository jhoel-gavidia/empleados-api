package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.dto.request.LoginRequest;
import com.jhoel.empleados_api.dto.response.AuthResponse;
import com.jhoel.empleados_api.entity.User;
import com.jhoel.empleados_api.service.Interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        return AuthResponse.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}

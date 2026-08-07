package com.jhoel.empleados_api.service.Impl;

import com.jhoel.empleados_api.dto.request.LoginRequest;
import com.jhoel.empleados_api.dto.response.AuthResponse;
import com.jhoel.empleados_api.entity.User;
import com.jhoel.empleados_api.security.JwtService;
import com.jhoel.empleados_api.service.Interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}

package com.jhoel.empleados_api.service.Interfaces;

import com.jhoel.empleados_api.dto.request.LoginRequest;
import com.jhoel.empleados_api.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
}

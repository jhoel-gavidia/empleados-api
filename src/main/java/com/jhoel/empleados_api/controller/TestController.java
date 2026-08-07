package com.jhoel.empleados_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {


    @GetMapping("/admin")
    public String admin() {
        return "Solo ADMIN";
    }


    @GetMapping("/user")
    public String user() {
        return "Usuario autenticado";
    }
}

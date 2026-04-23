package com.hospital.hospitalmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // ఇది src/main/resources/templates/login.html ని వెతుకుతుంది
    }
}
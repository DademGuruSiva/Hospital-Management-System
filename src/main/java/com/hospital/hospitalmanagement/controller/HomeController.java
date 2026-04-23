package com.hospital.hospitalmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

//    @GetMapping("/")
    public String home() {
        return "index";   // this loads index.html
    }
//    
 // View Patients page
    @GetMapping("/viewPatients")
    public String viewPatients() {
        return "viewPatients";  // loads viewPatients.html
    }
}


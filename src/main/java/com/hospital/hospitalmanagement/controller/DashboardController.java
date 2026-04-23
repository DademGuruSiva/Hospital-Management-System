

package com.hospital.hospitalmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.UserRepository;
import com.hospital.hospitalmanagement.repository.DoctorRepository; // యాడ్ చేశాను
import com.hospital.hospitalmanagement.repository.AppointmentRepository; // యాడ్ చేశాను

import java.util.ArrayList;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DoctorRepository docRepo; // ఆటోవైర్ చేశాను

    @Autowired
    private AppointmentRepository appointmentRepo; // ఆటోవైర్ చేశాను

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboardRedirect(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/adminDashboard";
        } else {
            return "redirect:/patientDashboard";
        }
    }

    @GetMapping("/adminDashboard")
    public String showAdminDashboard(Model model) {
        // అడ్మిన్ కి కావాల్సిన కౌంట్స్ ఇక్కడ సెట్ చేయాలి
        model.addAttribute("totalDoctors", docRepo.count());
        model.addAttribute("totalAppts", appointmentRepo.count());
        model.addAttribute("recentAppointments", appointmentRepo.findAll());
        return "admin_dashboard"; 
    }

    // --- ఇక్కడ మార్పు చేశాను (Patient Dashboard) ---
    @GetMapping("/patientDashboard")
    public String showPatientDashboard(Model model, Authentication authentication) {
        // 1. డాక్టర్ల లిస్ట్ పంపడం (దీనివల్ల Cards కనిపిస్తాయి)
        model.addAttribute("doctors", docRepo.findAll());

        // 2. పేషెంట్ అపాయింట్‌మెంట్స్ పంపడం (దీనివల్ల Table కనిపిస్తుంది)
        model.addAttribute("myAppointments", appointmentRepo.findAll());

        // 3. లాగిన్ అయిన యూజర్ పేరు పంపడం
        if (authentication != null) {
            model.addAttribute("patientName", authentication.getName());
        } else {
            model.addAttribute("patientName", "Guest");
        }

        return "patient_dashboard"; 	
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); 
        userRepo.save(user);
        return "redirect:/login?success";
    }
}
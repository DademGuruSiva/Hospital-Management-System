package com.hospital.hospitalmanagement;

import com.hospital.hospitalmanagement.model.User;
import com.hospital.hospitalmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // డేటాబేస్ లో యూజర్లు ఎవరూ లేకపోతేనే అడ్మిన్ ని క్రియేట్ చేస్తుంది
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            // పాస్‌వర్డ్ ని ఎన్‌క్రిప్ట్ చేసి సేవ్ చేస్తున్నాం
            admin.setPassword(passwordEncoder.encode("admin123")); 
            admin.setRole("ROLE_ADMIN");
            
            userRepository.save(admin);
            System.out.println("Default Admin User Created: username=admin, password=admin123");
        }
    }
}
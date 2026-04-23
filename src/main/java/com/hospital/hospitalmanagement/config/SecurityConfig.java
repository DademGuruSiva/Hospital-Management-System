//package com.hospital.hospitalmanagement.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) 
//            .authorizeHttpRequests(auth -> auth
//                // అందరికీ పర్మిషన్ ఉండే URLలు (Static resources and Login)
//                .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register").permitAll() 
//                
//                // అడ్మిన్ కి మాత్రమే పర్మిషన్ ఉండాల్సిన URLలు
//                .requestMatchers("/admin/**", "/adminDashboard").hasRole("ADMIN")
//                
//                .anyRequest().authenticated() 
//            )
//            .formLogin(login -> login
//                .loginPage("/login")
//                // ఇక్కడ గమనించు: '/dashboard' కి పంపిస్తున్నాం. 
//                // మన LoginController అక్కడ రోల్ ని చెక్ చేసి అడ్మిన్ డాష్‌బోర్డ్ కి పంపిస్తుంది.
//                .defaultSuccessUrl("/dashboard", true) 
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout") 
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//            );
//
//        return http.build(); 
//    }
//}



package com.hospital.hospitalmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                // 1. అందరికీ పర్మిషన్ ఉండేవి (Static Files, Login, Register)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/login", "/register", "/").permitAll() 
                
                // 2. అడ్మిన్ కి మాత్రమే పర్మిషన్ ఉండాల్సినవి
                .requestMatchers("/admin/**", "/adminDashboard", "/addDoctor", "/doctorList", "/saveDoctor").hasRole("ADMIN")
                
                // 3. పేషెంట్ కి పర్మిషన్ ఉండాల్సినవి (ఇది చాలా ముఖ్యం!)
                .requestMatchers("/patientDashboard", "/patient/pay-step/**", "/patient/confirm-booking").hasAnyRole("USER", "ADMIN")
                
                // 4. మిగిలిన ఏ URL అయినా సరే లాగిన్ అయితేనే ఓపెన్ అవ్వాలి
                .anyRequest().authenticated() 
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true) // ఇక్కడ DashboardController రోల్ ని బట్టి రీడైరెక్ట్ చేస్తుంది
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") 
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build(); 
    }
}
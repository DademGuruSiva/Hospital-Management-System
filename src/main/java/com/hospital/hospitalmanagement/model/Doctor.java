


package com.hospital.hospitalmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", unique = true)
    private String doctorId; 
    
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String experience;

    @Column(name = "consultation_fee")
    private double consultationFee;

    private boolean available;

    // --- Added fields for Doctor Dashboard Login ---
    
    @Column(unique = true) 
    private String username; // Unique username for doctor login

    private String password; // Secret password for dashboard access

    private String role = "DOCTOR"; // Identifies the user role for security

    // --- Getters and Setters for existing fields ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    // --- Getters and Setters for New Dashboard fields ---

    public String getUsername() { return username; } // Getter for login username
    public void setUsername(String username) { this.username = username; } // Setter for login username

    public String getPassword() { return password; } // Getter for login password
    public void setPassword(String password) { this.password = password; } // Setter for login password

    public String getRole() { return role; } // Getter for user role
    public void setRole(String role) { this.role = role; } // Setter for user role
}
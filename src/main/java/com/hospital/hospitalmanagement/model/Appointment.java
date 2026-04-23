

package com.hospital.hospitalmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appointmentId;
    
    private String patientName;
    private String patientPhone;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient; 

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    
    @Column(length = 500)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private String status; // Pending, Confirmed

    // --- PATIENT DASHBOARD & PAYMENT FIELDS (START) ---
    private String paymentMode;   // UPI, Card, Cash
    private double amountPaid;    // Doctor Consultation Fee
    private String paymentStatus; // Paid, Pending
    // --- PATIENT DASHBOARD & PAYMENT FIELDS (END) ---

    private String doctorAvailable; 

    public String getDoctorAvailable() { 
        return doctorAvailable; 
    }
    
    public void setDoctorAvailable(String doctorAvailable) { 
        this.doctorAvailable = doctorAvailable; 
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientPhone() { return patientPhone; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- NEW GETTERS & SETTERS FOR PAYMENT ---
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}
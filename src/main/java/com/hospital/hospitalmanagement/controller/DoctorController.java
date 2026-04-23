


package com.hospital.hospitalmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // యాడ్ చేశాను
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.hospitalmanagement.model.Appointment;
import com.hospital.hospitalmanagement.model.Doctor;
import com.hospital.hospitalmanagement.repository.AppointmentRepository;
import com.hospital.hospitalmanagement.repository.DoctorRepository;

@Controller
public class DoctorController {

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    // --- ADMIN METHODS ---
    @GetMapping("/addDoctor")
    public String showDoctorForm(Model model) {
        if (!model.containsAttribute("doctor")) {
            model.addAttribute("doctor", new Doctor());
        }
        return "doctor_form"; 
    }

    @PostMapping("/saveDoctor")
    public String saveDoctor(@ModelAttribute("doctor") Doctor d, RedirectAttributes ra) {
        try {
            if (d.getId() == null) {
                Long maxId = docRepo.findMaxId();
                long nextId = (maxId == null) ? 1 : maxId + 1;
                String formattedId = String.format("DOC-%d-%04d", LocalDate.now().getYear(), nextId);
                d.setDoctorId(formattedId); 
            }
            docRepo.save(d); 
            ra.addFlashAttribute("showPopup", true);
            ra.addFlashAttribute("successMsg", "Doctor Details Updated Successfully!");
            return "redirect:/doctorList"; 
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/addDoctor";
        }
    }

    @GetMapping("/doctorList")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", docRepo.findAll());
        return "doctor_list"; 
    }

    @GetMapping("/deleteDoctor/{id}")
    public String deleteDoctor(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            docRepo.deleteById(id);
            ra.addFlashAttribute("successMsg", "Doctor Deleted Successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: Could not delete doctor.");
        }
        return "redirect:/doctorList";
    }

    // --- DOCTOR LOGIN ---
    @GetMapping("/doctor/login")
    public String showDoctorLoginPage() {
        return "doctor_login"; 
    }

    @PostMapping("/doctor/doLogin")
    public String loginDoctor(@RequestParam String username, @RequestParam String password, Model model) {
        Doctor doctor = docRepo.findByUsernameAndPassword(username, password);
        if (doctor != null) {
            List<Appointment> myAppointments = appointmentRepo.findByDoctorName(doctor.getName());
            model.addAttribute("appointments", myAppointments);
            model.addAttribute("docName", doctor.getName()); 
            return "doctor_dashboard"; 
        } else {
            model.addAttribute("error", "Invalid Username or Password!");
            return "doctor_login";
        }
    }

    // --- PAYMENT & BOOKING LOGIC ---

    @GetMapping("/patient/pay-step/{id}")
    public String showPaymentPage(@PathVariable("id") Long id, Model model) {
        Optional<Doctor> doctor = docRepo.findById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "payment_gateway"; 
        }
        return "redirect:/patientDashboard"; 
    }
    
 // Edit ఫామ్ ఓపెన్ చేయడానికి ఈ మెథడ్ కావాలి
    @GetMapping("/editDoctor/{id}")
    public String editDoctor(@PathVariable("id") Long id, Model model) {
        Optional<Doctor> doctor = docRepo.findById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctor_form"; // ఇది నీ పాత డాక్టర్ ఫామ్ నే వాడుకుంటుంది
        }
        return "redirect:/doctorList";
    }

    @PostMapping("/patient/confirm-booking")
    public String confirmBooking(
            @RequestParam Long doctorId, 
            @RequestParam String paymentMode,
            @RequestParam double amount,
            Authentication authentication, // లాగిన్ అయిన యూజర్ కోసం ఇది యాడ్ చేశాను
            RedirectAttributes ra) {
        
        try {
            Appointment appt = new Appointment();
            Optional<Doctor> docOpt = docRepo.findById(doctorId);
            
            if(docOpt.isPresent()) {
                Doctor doc = docOpt.get();
                appt.setDoctor(doc);
                appt.setAmountPaid(amount);
                appt.setPaymentMode(paymentMode);
                appt.setAppointmentDate(LocalDate.now()); 
                appt.setStatus("Confirmed");
                appt.setPaymentStatus("Paid");
                
                // ఇక్కడ 'Online Patient' బదులు లాగిన్ అయిన యూజర్ పేరు పెడుతున్నాం
                if (authentication != null) {
                    appt.setPatientName(authentication.getName());
                } else {
                    appt.setPatientName("Guest Patient");
                }
                
                appointmentRepo.save(appt);
                ra.addFlashAttribute("successMsg", "Appointment Confirmed & Fee Paid!");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Booking failed! Try again.");
        }
        return "redirect:/patientDashboard"; 
    }
}
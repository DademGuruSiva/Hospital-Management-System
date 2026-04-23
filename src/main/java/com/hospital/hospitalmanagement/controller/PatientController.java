package com.hospital.hospitalmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.hospitalmanagement.model.Patient;
import com.hospital.hospitalmanagement.model.Appointment;
import com.hospital.hospitalmanagement.repository.PatientRepository;
import com.hospital.hospitalmanagement.repository.AppointmentRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientRepository repo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    // 1. రోగిని చేర్చడానికి లేదా ఎడిట్ చేయడానికి ఫారమ్
    @GetMapping("/addPatient")
    public String showForm(Model model) {
        if (!model.containsAttribute("patient")) {
            model.addAttribute("patient", new Patient());
        }
        return "patient_form"; 
    }

    // 2. సెర్చ్ ఫంక్షనాలిటీ (Dashboard లో సెర్చ్ చేస్తే పనిచేస్తుంది)
    @GetMapping("/searchPatient")
    public String searchPatient(@RequestParam("query") String query, Model model) {
        try {
            // పేషెంట్ పేరుతో అపాయింట్‌మెంట్‌లను వెతకడం
            List<Appointment> results = appointmentRepo.findByPatientNameContainingIgnoreCase(query);
            model.addAttribute("recentAppointments", results); 

            // డాష్‌బోర్డ్ కార్డుల కోసం డేటా
            model.addAttribute("totalPatients", repo.count());
            model.addAttribute("totalAppts", appointmentRepo.count());
            model.addAttribute("totalDoctors", 8);  // Dummy - Later get from doctorRepo
            model.addAttribute("totalBills", 14);    // Dummy
            model.addAttribute("totalMeds", 3);     // Dummy

            return "admin_dashboard"; 
            
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/adminDashboard"; 
        }
    }

    // 3. సేవ్ మరియు అప్‌డేట్ లాజిక్
    @PostMapping("/savePatient")
    public String savePatient(@ModelAttribute("patient") Patient p, RedirectAttributes redirectAttrs) {
        try {
            if (p.getId() == null) {
                // కొత్త పేషెంట్ - ID జనరేషన్
                Long maxId = repo.findMaxId();
                long nextId = (maxId == null) ? 1 : maxId + 1;
                
                String formattedId = String.format("PAT-%d-%04d", LocalDate.now().getYear(), nextId);
                p.setPatientId(formattedId);
                p.setAdmittedDate(LocalDate.now());
                redirectAttrs.addFlashAttribute("successMsg", "Patient Registered Successfully! ID: " + formattedId);
            } else {
                // అప్‌డేట్ చేస్తున్నప్పుడు పాత ID & Date మారకుండా చూసుకోవాలి
                Patient existingPatient = repo.findById(p.getId()).orElse(null);
                if (existingPatient != null) {
                    p.setPatientId(existingPatient.getPatientId());
                    p.setAdmittedDate(existingPatient.getAdmittedDate());
                }
                redirectAttrs.addFlashAttribute("successMsg", "Patient Details Updated Successfully!");
            }

            repo.save(p);
            redirectAttrs.addFlashAttribute("showPopup", true);
            redirectAttrs.addFlashAttribute("generatedId", p.getPatientId());
            
            return "redirect:/patientList"; 

        } catch (Exception e) {
            e.printStackTrace(); 
            redirectAttrs.addFlashAttribute("errorMsg", "Something went wrong!");
            return "redirect:/addPatient";
        }
    }

    // 4. పేషెంట్స్ అందరిని టేబుల్ లో చూపించడం
    @GetMapping("/patientList")
    public String list(Model model) {
        try {
            List<Patient> listPatients = repo.findAll();
            model.addAttribute("patients", listPatients); 
            return "patient_list"; 
        } catch (Exception e) {
            e.printStackTrace(); 
            return "redirect:/addPatient"; 
        }
    }

    // 5. పేషెంట్‌ని డిలీట్ చేయడం
    @GetMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable("id") Long id, RedirectAttributes redirectAttrs) {
        try {
            repo.deleteById(id);
            redirectAttrs.addFlashAttribute("successMsg", "Patient Deleted Successfully!");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMsg", "Error deleting patient!");
        }
        return "redirect:/patientList"; 
    }

    // 6. పేషెంట్ డేటాను ఎడిట్ చేయడానికి ఫారమ్‌కు పంపడం
    @GetMapping("/editPatient/{id}")
    public String editPatient(@PathVariable("id") Long id, Model model) {
        Patient p = repo.findById(id).orElse(null);
        if (p != null) {
            model.addAttribute("patient", p);
            return "patient_form"; 
        }
        return "redirect:/patientList";
    }
}
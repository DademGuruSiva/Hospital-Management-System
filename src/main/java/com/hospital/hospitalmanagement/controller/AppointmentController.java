

package com.hospital.hospitalmanagement.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.hospitalmanagement.model.Appointment;
import com.hospital.hospitalmanagement.repository.AppointmentRepository;
import com.hospital.hospitalmanagement.repository.BillRepository;
import com.hospital.hospitalmanagement.repository.DoctorRepository;
import com.hospital.hospitalmanagement.repository.MedicineRepository;
import com.hospital.hospitalmanagement.repository.PatientRepository;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appRepo;

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private MedicineRepository medicineRepo; 
    
    @Autowired
    private BillRepository billRepo; 

//    @GetMapping("/adminDashboard")
//    public String showAdminDashboard(Model model) {
//        model.addAttribute("totalPatients", patientRepo.count());
//        model.addAttribute("totalDoctors", docRepo.count());
//        model.addAttribute("totalAppts", appRepo.count());
//        model.addAttribute("totalBills", billRepo.count());
//        model.addAttribute("totalMedicines", medicineRepo.count()); 
//        model.addAttribute("recentAppointments", appRepo.findAll()); 
//        return "admin_dashboard";
//    }

    @GetMapping("/deleteAppointment/{id}")
    public String deleteAppointment(@PathVariable(value = "id") Long id, RedirectAttributes ra) {
        try {
            appRepo.deleteById(id);
            ra.addFlashAttribute("successMsg", "Appointment deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Failed to delete appointment.");
        }
        return "redirect:/adminDashboard"; // ఇక్కడ మార్చాను
    }

    @GetMapping("/editAppointment/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Appointment> appt = appRepo.findById(id);
        if (appt.isPresent()) {
            model.addAttribute("appointment", appt.get());
            model.addAttribute("doctors", docRepo.findAll());
            return "appointment_form"; 
        }
        return "redirect:/adminDashboard";
    }

    @GetMapping("/addAppointment")
    public String showAppointmentForm(Model model) {
        if (!model.containsAttribute("appointment")) {
            model.addAttribute("appointment", new Appointment());
        }
        model.addAttribute("doctors", docRepo.findAll());
        return "appointment_form";
    }
    
 // --- కేవలం ఈ కింద ఉన్న మెథడ్స్ మాత్రమే యాడ్ చెయ్ ---

    @GetMapping("/approveAppointment/{id}")
    public String approveAppointment(@PathVariable("id") Long id, RedirectAttributes ra) {
        Optional<Appointment> apptOpt = appRepo.findById(id);
        if (apptOpt.isPresent()) {
            Appointment appt = apptOpt.get();
            appt.setStatus("Success"); // Status ని Success గా మారుస్తుంది
            appRepo.save(appt);
            ra.addFlashAttribute("successMsg", "Appointment Approved Successfully!");
        }
        return "redirect:/adminDashboard";
    }

    @GetMapping("/cancelAppointment/{id}")
    public String cancelAppointment(@PathVariable("id") Long id, RedirectAttributes ra) {
        Optional<Appointment> apptOpt = appRepo.findById(id);
        if (apptOpt.isPresent()) {
            Appointment appt = apptOpt.get();
            appt.setStatus("Cancelled"); // Status ని Cancelled గా మారుస్తుంది
            appRepo.save(appt);
            ra.addFlashAttribute("successMsg", "Appointment Cancelled!");
        }
        return "redirect:/adminDashboard";
    }
    
   

    @PostMapping("/saveAppointment")
    public String saveAppointment(@ModelAttribute("appointment") Appointment a, RedirectAttributes ra) {
        try {
            if (a.getId() == null) {
                Long maxId = appRepo.findMaxId(); 
                long nextId = (maxId == null) ? 1 : maxId + 1;
                String formattedId = String.format("APP-%d-%04d", LocalDate.now().getYear(), nextId);
                a.setAppointmentId(formattedId);
                
                if(a.getStatus() == null) {
                    a.setStatus("Pending");
                }
            }
            appRepo.save(a);
            ra.addFlashAttribute("successMsg", "Appointment Processed Successfully!");
            return "redirect:/adminDashboard"; // ఇక్కడ ముఖ్యమైన మార్పు!
        } catch (Exception e) {
            return "redirect:/addAppointment";
        }
    }

    @GetMapping("/appointmentList")
    public String showAppointmentList(Model model) {
        model.addAttribute("appointments", appRepo.findAll());
        return "appointment_list"; 
    }
}
package com.hospital.hospitalmanagement.controller;

import com.hospital.hospitalmanagement.model.Medicine;
import com.hospital.hospitalmanagement.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
public class PharmacyController {

    @Autowired
    private MedicineRepository medicineRepo;

    @GetMapping("/addMedicine")
    public String showForm(Model model) {
        if (!model.containsAttribute("medicine")) {
            model.addAttribute("medicine", new Medicine());
        }
        return "add_medicine";
    }

    @PostMapping("/saveMedicine")
    public String save(@ModelAttribute("medicine") Medicine med, RedirectAttributes ra) {
        try {
            // New medicine ayithe ID generate chesthundhi, Edit ayithe unnadi unchuthundhi
            if(med.getId() == null) {
                Long maxId = medicineRepo.findMaxId();
                long nextId = (maxId == null) ? 1 : maxId + 1;
                med.setMedicineId(String.format("MED-%d-%04d", LocalDate.now().getYear(), nextId));
            }
            medicineRepo.save(med);
            ra.addFlashAttribute("showPopup", true);
            ra.addFlashAttribute("successMsg", "Medicine Data Saved!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error: " + e.getMessage());
        }
        return "redirect:/medicineList";
    }

    @GetMapping("/medicineList")
    public String list(Model model) {
        model.addAttribute("medicines", medicineRepo.findAll());
        return "medicine_list";
    }

    // --- EDIT ACTION ---
    @GetMapping("/editMedicine/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Medicine med = medicineRepo.findById(id).get();
        model.addAttribute("medicine", med);
        return "add_medicine"; // Add medicine form ne Edit ki kuda vaduthunnam
    }

    // --- DELETE ACTION ---
    @GetMapping("/deleteMedicine/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        medicineRepo.deleteById(id);
        ra.addFlashAttribute("showDeletePopup", true);
        return "redirect:/medicineList";
    }
}
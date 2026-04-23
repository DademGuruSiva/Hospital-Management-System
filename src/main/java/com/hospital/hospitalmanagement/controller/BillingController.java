package com.hospital.hospitalmanagement.controller;

import com.hospital.hospitalmanagement.model.*;
import com.hospital.hospitalmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BillingController {

    @Autowired private BillRepository billRepo;
    @Autowired private MedicineRepository medRepo;
    @Autowired private PatientRepository patientRepo;

    @GetMapping("/consultationBill")
    public String consultationBill(Model model) {
        Bill bill = new Bill();
        bill.setBillType("Consultation");
        model.addAttribute("bill", bill);
        model.addAttribute("patients", patientRepo.findAll());
        model.addAttribute("medicines", medRepo.findAll());
        return "consultation_bill";
    }
    @GetMapping("/billingList")
    public String billingList(Model model) {
        model.addAttribute("bills", billRepo.findAll());
        return "billing_list"; 
    }
    @GetMapping("/viewBill/{id}")
    public String viewBill(@PathVariable("id") Long id, Model model) {
        
        Bill bill = billRepo.findById(id).orElse(null);
        
        if (bill != null) {
            model.addAttribute("bill", bill);
            return "view_bill"; // 
        } else {
            return "redirect:/billingList"; 
        }
    }
 
    @GetMapping("/serviceOrder") 
    public String newServiceOrder(Model model) {
        Bill bill = new Bill();
        bill.setBillType("Service Order");
        model.addAttribute("bill", bill);
        model.addAttribute("patients", patientRepo.findAll());
        return "service_bill"; 
    }
    @GetMapping("/billingReport")
    public String billingReport(Model model) {
        List<Bill> allBills = billRepo.findAll();
        
        
        double totalRevenue = allBills.stream().mapToDouble(Bill::getTotalAmount).sum();
        
        model.addAttribute("bills", allBills);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalBills", allBills.size());
        
        return "billing_report"; 
    }
    @GetMapping("/doctorReport")
    public String doctorReport(Model model) {
        List<Bill> allBills = billRepo.findAll();
        
        
        java.util.Map<String, Double> doctorRevenue = allBills.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                b -> b.getDoctorName().trim().toUpperCase(), 
                java.util.stream.Collectors.summingDouble(Bill::getTotalAmount)
            ));
            
        model.addAttribute("doctorRevenue", doctorRevenue);
        return "doctor_report"; 
    }
    
    @GetMapping("/patientReport")
    public String patientReport(Model model) {
        List<Bill> allBills = billRepo.findAll();
        
       
        java.util.Map<String, Double> patientRevenue = allBills.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                b -> b.getPatientName().trim().toUpperCase(), 
                java.util.stream.Collectors.summingDouble(Bill::getTotalAmount)
            ));
            
        model.addAttribute("patientRevenue", patientRevenue);
        return "patient_report"; 
    }
    
    @Autowired
    private AppointmentRepository appointmentRepo; 

//    @GetMapping("/appointmentReport")
//    public String appointmentReport(Model model) {
//        
//        List<Appointment> appointments = appointmentRepo.findAll();
//        
//        model.addAttribute("appointments", appointments);
//        model.addAttribute("totalAppointments", appointments.size());
//        
//        return "appointment_report"; 
//    }
//    
    @PostMapping("/saveBill")
    public String saveBill(@ModelAttribute Bill bill, 
                           @RequestParam(required = false) List<String> itemNames,
                           @RequestParam(required = false) List<Integer> itemQtys,
                           @RequestParam(required = false) List<Double> itemPrices) {
        
        // 1. Bill ID Generation (పాత కోడ్ లాగే)
        Long maxId = billRepo.findMaxId();
        long nextId = (maxId == null) ? 1001 : maxId + 1;
        bill.setBillId("BILL-" + LocalDate.now().getYear() + "-" + nextId);

        if (itemNames != null) {
            for (int i = 0; i < itemNames.size(); i++) {
                String name = itemNames.get(i);
                Integer qty = itemQtys.get(i);
                Double price = itemPrices.get(i);

                
               
                if ("Consultation".equalsIgnoreCase(bill.getBillType())) {
                    Medicine med = medRepo.findByName(name);
                    if (med != null) {
                        int currentStock = med.getStockQuantity();
                        if (currentStock >= qty) {
                            med.setStockQuantity(currentStock - qty);
                            medRepo.save(med);
                        }
                    }
                }

                // Bill Item 
                BillItem item = new BillItem();
                item.setItemName(name);
                item.setQuantity(qty);
                item.setUnitPrice(price);
                item.setTotalPrice(qty * price);
                item.setBill(bill);
                bill.getItems().add(item);
            }
        }
        
        billRepo.save(bill);
        return "redirect:/billingList";
    }
}
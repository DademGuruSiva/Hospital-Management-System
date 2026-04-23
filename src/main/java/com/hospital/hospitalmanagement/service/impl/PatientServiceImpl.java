package com.hospital.hospitalmanagement.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.hospitalmanagement.model.Patient;import com.hospital.hospitalmanagement.repository.PatientRepository;
import com.hospital.hospitalmanagement.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = getPatientById(id);

        existingPatient.setName(patient.getName());
        existingPatient.setAge(patient.getAge());
        existingPatient.setGender(patient.getGender());
        existingPatient.setPhone(patient.getPhone());
        existingPatient.setAddress(patient.getAddress());

        return patientRepository.save(existingPatient);
    }

    @Override
    public void deletePatient(Long id) {
       Patient patient = getPatientById(id);
       patientRepository.delete(patient);
   }
}



package com.hospital.hospitalmanagement.service;

import java.util.List;
import com.hospital.hospitalmanagement.model.Patient;

public interface PatientService {

    Patient savePatient(Patient patient);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);
}

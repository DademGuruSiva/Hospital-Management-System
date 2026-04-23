

package com.hospital.hospitalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.hospital.hospitalmanagement.model.Patient;
import java.util.List; 
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // patient  ID (Search Feature)
    List<Patient> findByPatientIdContainingOrFirstNameContainingIgnoreCase(String patientId, String firstName);

    // Database largest id find
    @Query("SELECT MAX(p.id) FROM Patient p")
    Long findMaxId();
}


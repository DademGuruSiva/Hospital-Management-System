

package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // 1. To find the maximum ID for auto-generating Doctor IDs (Existing)
    @Query("SELECT MAX(d.id) FROM Doctor d")
    Long findMaxId();

    // 2. Logic to verify doctor credentials during login (New)
    // This will check if username and password match in the database
    Doctor findByUsernameAndPassword(String username, String password);
}
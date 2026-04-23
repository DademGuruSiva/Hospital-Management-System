

package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. To find the maximum ID for auto-generating Appointment IDs
    @Query("SELECT COALESCE(MAX(a.id), 0) FROM Appointment a")
    Long findMaxId();

    // 2. Used for dashboard charts to count status (e.g., Success, Pending)
    long countByStatus(String status);

    // 3. To filter appointments between two dates for reports
    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);

    // 4. For search bar functionality (Search by Patient Name)
    List<Appointment> findByPatientNameContainingIgnoreCase(String patientName);

    // --- NEW METHOD FOR DOCTOR DASHBOARD ---

    // 5. This method will fetch all appointments assigned to a specific doctor
    // This is required by DoctorController to show doctor-specific patient list
    List<Appointment> findByDoctorName(String doctorName); 
}
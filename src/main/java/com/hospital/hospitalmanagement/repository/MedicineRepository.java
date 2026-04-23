package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    @Query("SELECT MAX(m.id) FROM Medicine m")
    Long findMaxId();

    // this line new medicine find it
    Medicine findByName(String name);
}
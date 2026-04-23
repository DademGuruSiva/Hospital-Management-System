package com.hospital.hospitalmanagement.repository;

import com.hospital.hospitalmanagement.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    
    @Query("SELECT max(b.id) FROM Bill b")
    Long findMaxId();
}
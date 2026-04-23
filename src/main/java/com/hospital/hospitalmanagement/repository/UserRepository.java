package com.hospital.hospitalmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.hospitalmanagement.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    
}
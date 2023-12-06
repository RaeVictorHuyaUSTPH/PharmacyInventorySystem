package com.alpha.pharmacyinventorymanagementsystem.repository;

import com.alpha.pharmacyinventorymanagementsystem.constant.Role;
import com.alpha.pharmacyinventorymanagementsystem.dto.UserDTO;
import com.alpha.pharmacyinventorymanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUserRole(Role userRole);
}

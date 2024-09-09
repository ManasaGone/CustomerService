package com.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entities.Customer;
@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer,Integer>{
            boolean existsByUsername(String username);
            Optional<Customer> findByUsername(String username);
            Optional<Customer> findByEmail(String email);
            boolean existsByEmail(String email);
			Optional<Customer> findById(int customerId);
            
            
}
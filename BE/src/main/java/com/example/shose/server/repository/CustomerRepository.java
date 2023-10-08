package com.example.shose.server.repository;


import com.example.shose.server.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}

package com.usaa.applicationdemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usaa.applicationdemo.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

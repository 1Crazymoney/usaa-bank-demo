package com.usaa.applicationdemo.controllers;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.usaa.applicationdemo.entities.Customer;
import com.usaa.applicationdemo.repositories.CustomerRepository;

@RestController
public class AddCustomer {
  @Autowired
  CustomerRepository repository;

  @PostMapping("/customers")
  ResponseEntity<SuccessfulResponse> addCustomer(@Valid @RequestBody Customer newCustomer) {
    repository.save(newCustomer);
    return ResponseEntity.ok(new SuccessfulResponse(newCustomer));
  }

  class SuccessfulResponse {
    public String type = "CustomerCreated";
    public long customer_id;
    public String name;
    public HashMap<String, String> links = new HashMap<String, String>();

    public SuccessfulResponse(Customer customer) {
      this.customer_id = customer.getId();
      this.name = customer.getName();
      this.links.put("AddCreditCard", String.format("/customers/%s/creditcardaccounts", this.customer_id));
    }
  }
}

package com.usaa.applicationdemo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;

@Entity
@TableGenerator(name = "c", initialValue = 7123477, allocationSize = 37) // random-like account number
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "c")
  private long id;

  @NotBlank(message = "Name is required")
  private String name;

  public Customer() {
  }

  public Customer(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }
}

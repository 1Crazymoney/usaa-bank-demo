package com.usaa.applicationdemo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.lang.Nullable;

@Entity
@TableGenerator(name = "cc", initialValue = 4325679, allocationSize = 21) // random-like account number
public class CreditCardAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cc")
  private long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @Min(value = 500, message = "Minimum credit limit is $500")
  @Max(value = 50000, message = "Maximum credit limit is $50,000")
  private long creditLimit; // for this demo, assume whole dollars
  @Nullable()
  private long availableCredit;

  public CreditCardAccount() {
  }

  public CreditCardAccount(long id, Customer customer, long creditLimit) {
    this.id = id;
    this.customer = customer;
    this.creditLimit = creditLimit;
    this.availableCredit = creditLimit;
  }

  public CreditCardAccount(long id, Customer customer, long creditLimit, long availableCredit) {
    this.id = id;
    this.customer = customer;
    this.creditLimit = creditLimit;
    this.availableCredit = availableCredit;
  }

  public void applyTransaction(Transaction transaction) throws Error {
    long requestedCredit = transaction.getValue();
    long remainingCredit = this.availableCredit - requestedCredit;

    if (remainingCredit < 0) {
      throw new CreditCardAccount.InsufficientCredit(requestedCredit, this.availableCredit);
    }

    this.availableCredit = remainingCredit;
  }

  public void loadCredit() {
    this.availableCredit = this.creditLimit;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public long getId() {
    return this.id;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public long getCreditLimit() {
    return this.creditLimit;
  }

  public long getAvailableCredit() {
    return this.availableCredit;
  }

  public class InsufficientCredit extends Error {
    long requested;

    InsufficientCredit(long requested, long available) {
      super(String.format("Insufficient credit available. Requested $%s, $%s available", requested, available));
      this.requested = requested;
    }
  }
}

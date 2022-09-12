package com.usaa.applicationdemo.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Transaction {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "credit_card_account_id")
  private CreditCardAccount creditCardAccount;

  @NotBlank(message = "Merchant name is required")
  private String merchant;

  @Min(0)
  private int value;

  public Transaction() {
  }

  public Transaction(UUID id, String merchant, int value) {
    this.id = id;
    this.merchant = merchant;
    this.value = value;
  }

  public UUID getId() {
    return this.id;
  }

  public String getMerchant() {
    return this.merchant;
  }

  public int getValue() {
    return this.value;
  }

  public void setAccount(CreditCardAccount account) {
    this.creditCardAccount = account;
  }
}

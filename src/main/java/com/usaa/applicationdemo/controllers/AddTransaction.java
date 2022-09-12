package com.usaa.applicationdemo.controllers;

import java.util.HashMap;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.usaa.applicationdemo.entities.CreditCardAccount;
import com.usaa.applicationdemo.entities.Transaction;
import com.usaa.applicationdemo.repositories.CreditCardAccountRepository;
import com.usaa.applicationdemo.repositories.TransactionRepository;

@RestController
public class AddTransaction {
  @Autowired
  CreditCardAccountRepository creditCardRepository;

  @Autowired
  TransactionRepository transactionRepository;

  @PostMapping("/creditcardaccounts/{account_id}/transactions")
  ResponseEntity<Object> addTransaction(
      @PathVariable("account_id") long account_id,
      @Valid @RequestBody Transaction newTransaction) {

    CreditCardAccount account = creditCardRepository.findById(account_id).orElseThrow(() -> {
      var message = String.format("The credit card account with id %s could not be found", account_id);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    });

    newTransaction.setAccount(account);

    try {
      account.applyTransaction(newTransaction);
    } catch (CreditCardAccount.InsufficientCredit error) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, error.getMessage());
    }

    transactionRepository.save(newTransaction);
    creditCardRepository.save(account);

    return ResponseEntity.ok(new SuccessfulResponse(newTransaction, account));
  }

  class SuccessfulResponse {
    public String type = "CreditCardAccountCreated";
    public UUID transaction_id;
    public String merchant;
    public long availableCreditRemaining;
    public long transactionValue;
    public HashMap<String, String> links = new HashMap<String, String>();

    public SuccessfulResponse(Transaction transaction, CreditCardAccount account) {
      this.transaction_id = transaction.getId();
      this.transactionValue = transaction.getValue();
      this.merchant = transaction.getMerchant();
      this.availableCreditRemaining = account.getAvailableCredit();
      this.links.put("AddTransaction", String.format("/creditcardaccounts/%s/transactions", account.getId()));
    }
  }
}

package com.maybank.mockapp.mockapp.controller;

import com.maybank.mockapp.mockapp.dto.AccountDTO;
import com.maybank.mockapp.mockapp.entity.Account;
import com.maybank.mockapp.mockapp.entity.Customer;
import com.maybank.mockapp.mockapp.exception.AccountNotFoundException;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.service.AccountService;
import com.maybank.mockapp.mockapp.service.CustomerService;
import jakarta.transaction.InvalidTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account/api")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService service;

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@RequestParam Long customerId,
                                                    @RequestParam String type) throws CustomerNotFoundException {
        logger.info("Request to create account for customerId: {}, type: {}", customerId, type);
        AccountDTO account = service.createAccount(customerId, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PutMapping("/deposit")
    public ResponseEntity<AccountDTO> deposit(@RequestParam Long accountNumber,
                                              @RequestParam double amount) throws AccountNotFoundException, InvalidTransactionException {
        logger.info("Request to deposit amount {} into account {}", amount, accountNumber);
        AccountDTO account = service.depositCash(accountNumber, amount);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@RequestParam Long accountNumber,
                                               @RequestParam double amount) throws AccountNotFoundException, InvalidTransactionException {
        logger.info("Request to withdraw amount {} from account {}", amount, accountNumber);
        AccountDTO account = service.withdrawCash(accountNumber, amount);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/close")
    public ResponseEntity<AccountDTO> closeAccount(@RequestParam Long accountNumber) throws AccountNotFoundException {
        logger.info("Request to close account {}", accountNumber);
        AccountDTO account = service.closeAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long accountNumber) throws AccountNotFoundException {
        logger.info("Request to inquire account {}", accountNumber);
        AccountDTO account = service.inquireAccount(accountNumber);
        return ResponseEntity.ok(account);
    }
}

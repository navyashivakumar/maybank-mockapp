package com.maybank.mockapp.mockapp.exception;

public class AccountNotFoundException  extends RuntimeException{

    public AccountNotFoundException(Long accountNumber) {
        super("Account not found with account number: " + accountNumber );
    }
}

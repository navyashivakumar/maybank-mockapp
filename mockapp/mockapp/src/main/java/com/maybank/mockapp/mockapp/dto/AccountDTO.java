package com.maybank.mockapp.mockapp.dto;

public class AccountDTO {

    private Long accountNumber;

    private String accountType;

    private String status = "Active";

    private double balance = 0.0;

    private Long customerId;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }

    public double getBalance() {
        return balance;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountDTO() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {

    }

    public AccountDTO(Long accountNumber, String accountType, String status, double balance, Long customerId) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.status = status;
        this.balance = balance;
        this.customerId = customerId;
    }
}

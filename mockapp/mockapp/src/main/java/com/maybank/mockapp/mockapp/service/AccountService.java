package com.maybank.mockapp.mockapp.service;

import com.maybank.mockapp.mockapp.dto.AccountDTO;
import com.maybank.mockapp.mockapp.entity.Account;
import com.maybank.mockapp.mockapp.entity.Customer;
import com.maybank.mockapp.mockapp.exception.AccountNotFoundException;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.repository.AccountRepository;
import com.maybank.mockapp.mockapp.repository.CustomerRepository;
import jakarta.transaction.InvalidTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public AccountDTO createAccount(Long customerId, String accountType) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        Account account = new Account();
        account.setAccountType(accountType);
        account.setCustomer(customer);
        account = accountRepository.save(account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), customer.getId());
    }

    public AccountDTO depositCash(Long accountNumber, double amount) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        account.setBalance(account.getBalance() + amount);
        account = accountRepository.save(account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

    public AccountDTO withdrawCash(Long accountNumber, double amount) throws AccountNotFoundException, InvalidTransactionException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (account.getBalance() < amount) {
            throw new InvalidTransactionException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        account = accountRepository.save(account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

    public AccountDTO closeAccount(Long accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        account.setStatus("Closed");
        account = accountRepository.save(account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

    public AccountDTO inquireAccount(Long accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

}

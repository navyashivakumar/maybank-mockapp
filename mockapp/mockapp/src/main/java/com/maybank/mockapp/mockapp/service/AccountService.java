package com.maybank.mockapp.mockapp.service;

import com.maybank.mockapp.mockapp.dto.AccountDTO;
import com.maybank.mockapp.mockapp.entity.Account;
import com.maybank.mockapp.mockapp.entity.Customer;
import com.maybank.mockapp.mockapp.exception.AccountNotFoundException;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.repository.AccountRepository;
import com.maybank.mockapp.mockapp.repository.CustomerRepository;
import jakarta.transaction.InvalidTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public AccountDTO createAccount(Long customerId, String accountType) throws CustomerNotFoundException {
        logger.info("Creating account for customerId={}, type={}", customerId, accountType);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.error("Customer with ID {} not found", customerId);
                    return new CustomerNotFoundException(customerId);
                });
        Account account = new Account();
        account.setAccountType(accountType);
        account.setCustomer(customer);
        account = accountRepository.save(account);
        logger.debug("Account created: {}", account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), customer.getId());
    }

    public AccountDTO depositCash(Long accountNumber, double amount) throws AccountNotFoundException, InvalidTransactionException {
        logger.info("Depositing amount={} to accountNumber={}", amount, accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    logger.error("Account with number {} not found for deposit", accountNumber);
                    return new AccountNotFoundException(accountNumber);
                });
        if (!account.getStatus().equals("Closed")) {
            account.setBalance(account.getBalance() + amount);
            account = accountRepository.save(account);
        }else{
            logger.warn("Account with number {} is closed", accountNumber);
            throw new InvalidTransactionException("Account is closed");
        }

        logger.debug("Deposit complete. New balance: {}", account.getBalance());
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

    public AccountDTO withdrawCash(Long accountNumber, double amount) throws AccountNotFoundException, InvalidTransactionException {
        logger.info("Attempting to withdraw amount={} from accountNumber={}", amount, accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    logger.error("Account with number {} not found for withdrawal", accountNumber);
                    return new AccountNotFoundException(accountNumber);
                });

        if (account.getBalance() < amount) {
            logger.warn("Insufficient balance for accountNumber={}. Available={}, Requested={}", accountNumber, account.getBalance(), amount);
            throw new InvalidTransactionException("Insufficient balance");
        }
        if (!account.getStatus().equals("Closed")) {
            account.setBalance(account.getBalance() - amount);
            account = accountRepository.save(account);
        } else{
            logger.warn("Account with number {} is closed", accountNumber);
            throw new InvalidTransactionException("Account is closed");
        }

        logger.debug("Withdrawal successful. Remaining balance: {}", account.getBalance());
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

    public AccountDTO closeAccount(Long accountNumber) throws AccountNotFoundException {
        logger.info("Closing account with accountNumber={}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    logger.error("Account with number {} not found for closure", accountNumber);
                    return new AccountNotFoundException(accountNumber);
                });

        account.setStatus("Closed");
        account = accountRepository.save(account);

        logger.debug("Account closed: {}", account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

    public AccountDTO inquireAccount(Long accountNumber) throws AccountNotFoundException {
        logger.info("Fetching details for accountNumber={}", accountNumber);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    logger.error("Account with number {} not found", accountNumber);
                    return new AccountNotFoundException(accountNumber);
                });

        logger.debug("Account found: {}", account);
        return new AccountDTO(account.getAccountNumber(), account.getAccountType(), account.getStatus(), account.getBalance(), account.getCustomer().getId());
    }

}

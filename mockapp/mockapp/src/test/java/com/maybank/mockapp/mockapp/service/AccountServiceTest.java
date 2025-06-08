package com.maybank.mockapp.mockapp.service;

import com.maybank.mockapp.mockapp.dto.AccountDTO;
import com.maybank.mockapp.mockapp.entity.Account;
import com.maybank.mockapp.mockapp.entity.Customer;
import com.maybank.mockapp.mockapp.exception.AccountNotFoundException;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.repository.AccountRepository;
import com.maybank.mockapp.mockapp.repository.CustomerRepository;
import jakarta.transaction.InvalidTransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        account = new Account();
        account.setAccountNumber(1001L);
        account.setAccountType("Savings");
        account.setStatus("Active");
        account.setBalance(1000.0);
        account.setCustomer(customer);
    }

    @Test
    void testCreateAccount_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountDTO dto = accountService.createAccount(1L, "Savings");
        assertEquals("Savings", dto.getAccountType());
        assertEquals(1L, dto.getCustomerId());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testCreateAccount_CustomerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class,
                () -> accountService.createAccount(99L, "Savings"));
    }

    @Test
    void testDepositCash_Success() {
        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountDTO dto = accountService.depositCash(1001L, 500.0);
        assertEquals(1500.0, dto.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void testDepositCash_AccountNotFound() {
        when(accountRepository.findByAccountNumber(999L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,
                () -> accountService.depositCash(999L, 100.0));
    }

    @Test
    void testWithdrawCash_Success() throws InvalidTransactionException {
        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountDTO dto = accountService.withdrawCash(1001L, 400.0);
        assertEquals(600.0, dto.getBalance());
    }

    @Test
    void testWithdrawCash_InsufficientFunds() {
        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(account));
        assertThrows(InvalidTransactionException.class,
                () -> accountService.withdrawCash(1001L, 2000.0));
    }

    @Test
    void testWithdrawCash_AccountNotFound() {
        when(accountRepository.findByAccountNumber(888L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,
                () -> accountService.withdrawCash(888L, 100.0));
    }

    @Test
    void testCloseAccount_Success() {
        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        AccountDTO dto = accountService.closeAccount(1001L);
        assertEquals("Closed", dto.getStatus());
    }

    @Test
    void testCloseAccount_AccountNotFound() {
        when(accountRepository.findByAccountNumber(777L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,
                () -> accountService.closeAccount(777L));
    }

    @Test
    void testInquireAccount_Success() {
        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(account));
        AccountDTO dto = accountService.inquireAccount(1001L);
        assertEquals(1001L, dto.getAccountNumber());
        assertEquals(1000.0, dto.getBalance());
    }

    @Test
    void testInquireAccount_AccountNotFound() {
        when(accountRepository.findByAccountNumber(111L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class,
                () -> accountService.inquireAccount(111L));
    }
}


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerRepository customerRepository;


    private Customer savedCustomer;

    @BeforeEach
    void setup() {
        Customer customer = new Customer();
        customer.setName("Test Customer");
        savedCustomer = customerRepository.save(customer);
    }

    @Test
    void testCreateAccount() throws CustomerNotFoundException {
        AccountDTO account = accountService.createAccount(savedCustomer.getId(), "Savings");
        assertNotNull(account.getAccountNumber());
        assertEquals("Savings", account.getAccountType());
        assertEquals(savedCustomer.getId(), account.getCustomerId());
    }

    @Test
    void testDepositCash() throws Exception {
        AccountDTO created = accountService.createAccount(savedCustomer.getId(), "Savings");
        AccountDTO updated = accountService.depositCash(created.getAccountNumber(), 1000.0);
        assertEquals(1000.0, updated.getBalance());
    }

    @Test
    void testWithdrawCash() throws Exception {
        AccountDTO created = accountService.createAccount(savedCustomer.getId(), "Savings");
        accountService.depositCash(created.getAccountNumber(), 2000.0);
        AccountDTO updated = accountService.withdrawCash(created.getAccountNumber(), 500.0);
        assertEquals(1500.0, updated.getBalance());
    }

    @Test
    void testWithdrawCash_InsufficientFunds() throws Exception {
        AccountDTO created = accountService.createAccount(savedCustomer.getId(), "Savings");

        assertThrows(InvalidTransactionException.class, () ->
                accountService.withdrawCash(created.getAccountNumber(), 100.0));
    }

    @Test
    void testCloseAccount() throws Exception {
        AccountDTO created = accountService.createAccount(savedCustomer.getId(), "Savings");

        AccountDTO closed = accountService.closeAccount(created.getAccountNumber());
        assertEquals("Closed", closed.getStatus());
    }

    @Test
    void testInquireAccount() throws Exception {
        AccountDTO created = accountService.createAccount(savedCustomer.getId(), "Savings");
        AccountDTO inquired = accountService.inquireAccount(created.getAccountNumber());
        assertEquals(created.getAccountNumber(), inquired.getAccountNumber());
        assertEquals("Savings", inquired.getAccountType());
    }

    @Test
    void testCreateAccountForNonExistingCustomer() {
        assertThrows(CustomerNotFoundException.class, () ->
                accountService.createAccount(99999L, "Savings"));
    }

    @Test
    void testInquireNonExistingAccount() {
        assertThrows(AccountNotFoundException.class, () ->
                accountService.inquireAccount(99999L));
    }
}

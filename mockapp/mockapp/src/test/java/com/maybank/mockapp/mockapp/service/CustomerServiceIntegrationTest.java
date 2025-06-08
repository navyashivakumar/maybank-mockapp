package com.maybank.mockapp.mockapp.service;

import com.maybank.mockapp.mockapp.dto.CustomerDTO;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testCreateCustomerAndFind() throws Exception {
        CustomerDTO created = customerService.createCustomer("Test User");
        assertNotNull(created.getId());
        CustomerDTO fetched = customerService.inquireCustomer(created.getId());
        assertEquals("Test User", fetched.getName());
    }

    @Test
    void testCustomerNotFound() {
        Long nonExistentId = 9999L;
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.inquireCustomer(nonExistentId);
        });
    }
}

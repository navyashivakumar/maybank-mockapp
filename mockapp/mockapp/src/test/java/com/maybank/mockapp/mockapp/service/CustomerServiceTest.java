package com.maybank.mockapp.mockapp.service;

import com.maybank.mockapp.mockapp.dto.CustomerDTO;
import com.maybank.mockapp.mockapp.entity.Customer;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepo;

    @InjectMocks
    private CustomerService customerService;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        mockCustomer = new Customer();
        mockCustomer.setId(1L);
        mockCustomer.setName("testcustomer");
    }

    @Test
    void testCreateCustomer_Success() {
        when(customerRepo.save(any(Customer.class))).thenReturn(mockCustomer);

        CustomerDTO result = customerService.createCustomer("testcustomer");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testcustomer", result.getName());
        verify(customerRepo).save(any(Customer.class));
    }

    @Test
    void testInquireCustomer_Success() throws CustomerNotFoundException {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomer));

        CustomerDTO result = customerService.inquireCustomer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testcustomer", result.getName());
    }

    @Test
    void testInquireCustomer_NotFound() {
        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.inquireCustomer(99L));
    }
}

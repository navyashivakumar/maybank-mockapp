package com.maybank.mockapp.mockapp.controller;

import com.maybank.mockapp.mockapp.dto.CustomerDTO;
import com.maybank.mockapp.mockapp.exception.CustomerNotFoundException;
import com.maybank.mockapp.mockapp.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    private CustomerController customerController;
    private CustomerService customerService;

    @BeforeEach
    void setUp() throws Exception {
        customerService = mock(CustomerService.class);
        customerController = new CustomerController();
        var serviceField = CustomerController.class.getDeclaredField("customerService");
        serviceField.setAccessible(true);
        serviceField.set(customerController, customerService);
    }

    @Test
    void testCreateCustomer_success() {
        CustomerDTO requestDTO = new CustomerDTO(null, "John Doe");
        CustomerDTO responseDTO = new CustomerDTO(1L, "John Doe");
        when(customerService.createCustomer("John Doe")).thenReturn(responseDTO);
        ResponseEntity<CustomerDTO> response = customerController.createCustomer(requestDTO);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testGetCustomer_success() throws Exception {
        Long customerId = 1L;
        CustomerDTO responseDTO = new CustomerDTO(customerId, "Jane Doe");
        when(customerService.inquireCustomer(customerId)).thenReturn(responseDTO);
        ResponseEntity<CustomerDTO> response = customerController.getCustomer(customerId);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jane Doe", response.getBody().getName());
    }

    @Test
    void testGetCustomer_notFound() throws Exception {
        Long customerId = 99L;
        when(customerService.inquireCustomer(customerId))
                .thenThrow(new CustomerNotFoundException(customerId));
        assertThrows(CustomerNotFoundException.class, () -> {
            customerController.getCustomer(customerId);
        });
    }
}

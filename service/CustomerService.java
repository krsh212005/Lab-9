package com.MAGNO__.LAB7.Service;

import com.MAGNO_.LAB7.Model.Customer;
import com.MAGNO__.LAB7.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // CREATE/SAVE
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    // READ ALL
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    // READ ONE
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    // NEW: UPDATE logic for GraphQL/REST
    public Optional<Customer> update(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Update all fields from the input DTO/entity
                    existingCustomer.setFirstName(updatedCustomer.getFirstName());
                    existingCustomer.setLastName(updatedCustomer.getLastName());
                    existingCustomer.setEmail(updatedCustomer.getEmail());

                    // Save the updated entity
                    return customerRepository.save(existingCustomer);
                });
    }

    // DELETE
    public boolean delete(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
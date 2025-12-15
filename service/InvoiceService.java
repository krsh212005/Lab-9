package com.MAGNO__.LAB7.Service;

import com.MAGNO__.LAB7.Model.Customer;
import com.MAGNO__.LAB7.Model.Invoice;
import com.MAGNO__.LAB7.Model.Product;
import com.MAGNO__.LAB7.repository.CustomerRepository;
import com.MAGNO__.LAB7.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductService productService;

    public InvoiceService(InvoiceRepository invoiceRepository, CustomerRepository customerRepository, ProductService productService) {
        this.invoiceRepository = invoiceRepository;
        this.customerRepository = customerRepository;
        this.productService = productService;
    }

    /**
     * Creates a new invoice, fetching the customer and products, and calculating the total.
     */
    public Optional<Invoice> createInvoice(Long customerId, Set<Long> productIds) {

        // 1. Find the Customer (Many-to-One relationship check)
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return Optional.empty(); // Customer not found
        }
        Customer customer = customerOpt.get();

        // 2. Fetch all Products (Many-to-Many relationship data)
        Set<Product> products = productIds.stream()
                .map(productService::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        // Simple check to ensure at least one valid product was found
        if (products.isEmpty() && !productIds.isEmpty()) {
            // Can add more specific error handling here if desired
            return Optional.empty();
        }

        // 3. Calculate the total
        double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        // 4. Create and save the Invoice entity
        Invoice invoice = new Invoice(customer, products, total);

        return Optional.of(invoiceRepository.save(invoice));
    }

    // READ ALL
    public List<Invoice> findAll() {
        return invoiceRepository.findAllEagerly(); // <-- Use the new EAGER fetch method
    }

    // READ ONE
    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }
}
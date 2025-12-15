package com.MAGNO_.LAB7.controller;

import com.MAGNO_.LAB7.Model.Invoice;
import com.MAGNO_.LAB7.Service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * A simple DTO (Data Transfer Object) used to map the incoming JSON request
 * for the POST /api/invoices endpoint.
 */
record InvoiceCreationRequest(Long customerId, Set<Long> productIds) {}

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    // Inject the InvoiceService dependency
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /*
     * POST /api/invoices
     * Creates an invoice using a customer ID and a list of product IDs.
     * * Request Body Example:
     * {
     * "customerId": 1,
     * "productIds": [1, 2]
     * }
     */
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceCreationRequest request) {

        // Delegate the complex creation logic to the service layer
        Optional<Invoice> invoiceOpt = invoiceService.createInvoice(request.customerId(), request.productIds());

        if (invoiceOpt.isPresent()) {
            // Success: returns the created invoice entity with HTTP status 201 CREATED
            return new ResponseEntity<>(invoiceOpt.get(), HttpStatus.CREATED);
        } else {
            // Failure: could be due to customer or product not found.
            // Return 400 BAD REQUEST to indicate invalid input data.
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * GET /api/invoices
     * Returns a list of all invoices.
     */
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return new ResponseEntity<>(invoiceService.findAll(), HttpStatus.OK);
    }

    /*
     * GET /api/invoices/{id}
     * Returns a single invoice by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return invoiceService.findById(id)
                .map(invoice -> new ResponseEntity<>(invoice, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

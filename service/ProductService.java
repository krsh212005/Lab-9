package com.MAGNO__.LAB7.Service;

import com.MAGNO_.LAB7.Model.Product;
import com.MAGNO_.LAB7.repository.ProductRepository; // Import the new repository
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transaction management
import java.util.List;
import java.util.Optional;

@Service
// @Transactional is good practice to ensure all operations are atomic
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // Inject the new JpaRepository instead of the mock map
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- CRUD Operations using Repository ---

    // 1. READ ALL Products (GET)
    public List<Product> findAll() {
        // Simple call to the repository
        return productRepository.findAll();
    }

    // 2. READ ONE Product by ID (GET)
    public Optional<Product> findById(Long id) {
        // JpaRepository's built-in findById returns an Optional
        return productRepository.findById(id);
    }

    // 3. CREATE/SAVE a new Product (POST)
    public Product save(Product product) {
        // JpaRepository's save handles both INSERT (if ID is null) and UPDATE (if ID is present)
        return productRepository.save(product);
    }

    // 4. UPDATE an existing Product (PUT)
    public Optional<Product> update(Long id, Product updatedProduct) {
        // Use findById to check if the product exists
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Update the fields on the existing entity
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    // Save the updated entity; JPA handles the UPDATE query
                    return productRepository.save(existingProduct);
                });
    }

    // 5. DELETE a Product
    public boolean delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false; // Or return true if you treat non-existence as a successful idempotent operation.
    }
}
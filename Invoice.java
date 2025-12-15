package com.MAGNO__.LAB7.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // <-- NEW IMPORT
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime issueDate = LocalDateTime.now();

    private Double total;

    // FIX 1: Exclude from Lombok's equals/hashCode to prevent ConcurrentModificationException
    @EqualsAndHashCode.Exclude
    // FIX 2: Ignore during JSON serialization to prevent LazyInitializationException
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // FIX 1: Exclude from Lombok's equals/hashCode to prevent ConcurrentModificationException
    @EqualsAndHashCode.Exclude
    // FIX 2: Ignore during JSON serialization to prevent LazyInitializationException
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "invoice_product",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    public Invoice(Customer customer, Set<Product> products, double total) {
        this.customer = customer;
        this.products = products;
        this.total = total;
    }
}
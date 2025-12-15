package com.MAGNO__.LAB7.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode; // <-- NEW IMPORT
import jakarta.persistence.*;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    // FIX 1: Exclude from Lombok's equals/hashCode to prevent ConcurrentModificationException
    @EqualsAndHashCode.Exclude
    // FIX 2: Ignore during JSON serialization to prevent LazyInitializationException
    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private Set<Invoice> invoices;
}
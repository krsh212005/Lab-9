package com.MAGNO_.LAB7.repository;

import com.MAGNO_.LAB7.Model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("SELECT i FROM Invoice i JOIN FETCH i.customer c JOIN FETCH i.products p")
    List<Invoice> findAllEagerly();

    List<Invoice> findByCustomerId(Long customerId);
}

package com.example.belajar_spring.repository;

import com.example.belajar_spring.model.Transaction;
import com.example.belajar_spring.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.buyer = :buyer")
    List<Transaction> findByBuyerOrderByTransactionDateDesc(@Param("buyer") User buyer, Sort sort);
    
    Long countByBuyer(User buyer);
}

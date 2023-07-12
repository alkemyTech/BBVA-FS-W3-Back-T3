package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long>  {

    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);



}

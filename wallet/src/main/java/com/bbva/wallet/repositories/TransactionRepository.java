package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {
    List<Transaction> findAllByAccount(Account usdAccount);

    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountId(Long accountId);
}
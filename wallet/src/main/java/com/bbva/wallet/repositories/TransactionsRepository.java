package com.bbva.wallet.repositories;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long>  {
    List<Transaction> findAllByAccount(Account usdAccount);
}

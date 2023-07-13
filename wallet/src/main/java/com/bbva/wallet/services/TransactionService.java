package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.exeptions.TransactionException;

import java.util.Optional;
public interface TransactionService {
    Transaction send(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount) throws TransactionException;
    Optional<Transaction> findById(Long id);
}
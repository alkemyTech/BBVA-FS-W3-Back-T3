package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionCreatedResponse;
import com.bbva.wallet.dtos.TransactionRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import java.util.List;
import com.bbva.wallet.exeptions.TransactionException;

import java.util.Optional;
public interface TransactionService {
    Transaction send(TransactionRequestDTO transactionDto, Account sourceAccount, Account destinationAccount) throws TransactionException;
    TransactionCreatedResponse deposit(Account account, Double amount, String description) throws TransactionException;
    TransactionCreatedResponse payment(Account sourceAccount, Double amount, String description) throws TransactionException;
    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
    List<Transaction> getUserTransaction(List<Account> accounts);
}
package com.bbva.wallet.services;

import com.bbva.wallet.dtos.DepositCreatedDTO;
import com.bbva.wallet.dtos.PaymentCreatedDTO;
import com.bbva.wallet.dtos.TransactionDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.TransactionException;

import java.util.Optional;
public interface TransactionService {
    Transaction send(TransactionDTO transactionDto, Account sourceAccount, Account destinationAccount) throws TransactionException;
    DepositCreatedDTO deposit(Account account, Double amount) throws TransactionException;
    PaymentCreatedDTO payment(Account sourceAccount, Double amount) throws TransactionException;
    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
}
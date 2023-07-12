package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import java.util.List;
import com.bbva.wallet.exeptions.TransactionException;

public interface TransactionService {
    Transaction send(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount)throws TransactionException;
    public List<Transaction> getUserTransaction(List<Account> accounts);
}
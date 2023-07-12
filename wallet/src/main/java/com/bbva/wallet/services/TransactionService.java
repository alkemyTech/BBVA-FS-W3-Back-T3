package com.bbva.wallet.services;

import com.bbva.wallet.dtos.DepositCreatedDTO;
import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exeptions.TransactionException;

public interface TransactionService {
    void send(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount) throws TransactionException;
    DepositCreatedDTO deposit(Account account, double amount) throws TransactionException;
}
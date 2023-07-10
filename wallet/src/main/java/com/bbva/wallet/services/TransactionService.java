package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.entities.User;

public interface TransactionService {
    void sendArs(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount);
}
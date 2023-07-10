package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.entities.User;

public interface TransactionService {
    Transactions sendArs(TransactionDto transactionDto, User userLoggedIn);

    Transactions sendUsd(TransactionDto transactionDto, User userLoggedIn);
}

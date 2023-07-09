package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Transactions;

public interface TransactionService {
    Transactions sendArs(TransactionDto transactionDto);
}

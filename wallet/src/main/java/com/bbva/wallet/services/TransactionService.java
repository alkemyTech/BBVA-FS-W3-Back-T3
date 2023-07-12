package com.bbva.wallet.services;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;


import java.util.List;



public interface TransactionService {


    void send(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount);

    public List<Transaction> getUserTransaction(List<Account> accounts);


}
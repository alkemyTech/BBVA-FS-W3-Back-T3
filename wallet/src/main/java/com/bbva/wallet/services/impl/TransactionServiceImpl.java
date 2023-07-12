package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TypeTransaction;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public void send(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount) {
        if (sourceAccount.getUser().equals(destinationAccount.getUser())) {
            return; // TODO: Agregar excepcion de que no se puede transferir a uno mismo
        }
        if (transactionDto.getAmount() > sourceAccount.getTransactionLimit()) {
            return; // TODO: Agregar excepcion de que no se puede transferir mas del limite
        }
        if (transactionDto.getAmount() > sourceAccount.getBalance()) {
            return; // TODO: Agregar excepcion de que no se puede transferir mas de lo que se tiene
        }
        if (!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
            return;// TODO: Agregar excepcion de que no se puede transferir a una cuenta de distinta moneda
        }

        var income = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.INCOME)
                .account(destinationAccount)
                .build();
        var payment = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.PAYMENT)
                .account(sourceAccount)
                .build();

        transactionRepository.save(income);
        transactionRepository.save(payment);

        Double newBalanceIncome = destinationAccount.getBalance() + transactionDto.getAmount();
        Double newBalancePayment = sourceAccount.getBalance() - transactionDto.getAmount();
        destinationAccount.setBalance(newBalanceIncome);
        sourceAccount.setBalance(newBalancePayment);
    }

    @Override
    public List<Transaction> getUserTransaction(List<Account> accounts) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        accounts.forEach(account -> {
            transactions.addAll(transactionRepository.findByAccountId(account.getId()));
        });
        return transactions;
    }
}
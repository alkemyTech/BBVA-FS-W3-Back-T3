package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.enums.TypeTransaction;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private AccountRepository accountRepository;
    public Transactions sendArs(TransactionDto transactionDto){
        Optional<Account> incomeAccount = accountRepository.findById(transactionDto.getDestinationAccountId());
        Optional<Account> paymentAccount = accountRepository.findById(transactionDto.getSourceAccountId());
        if (!incomeAccount.isPresent() || !paymentAccount.isPresent()){
            return null; //agregar manejos de errores
        }
        if (incomeAccount.get().getUser() == paymentAccount.get().getUser()){
            return null; // agregar manejo de errores
        }
        var income = Transactions.builder()
                .amount(transactionDto.getAmount())
                .transactionDate(transactionDto.getTransactionDate())
                .type(TypeTransaction.INCOME)
                .account(incomeAccount.get());
        var payment = Transactions.builder()
                .amount(transactionDto.getAmount())
                .transactionDate(transactionDto.getTransactionDate())
                .type(TypeTransaction.PAYMENT)
                .account(paymentAccount.get());
        return new Transactions();
    }
}
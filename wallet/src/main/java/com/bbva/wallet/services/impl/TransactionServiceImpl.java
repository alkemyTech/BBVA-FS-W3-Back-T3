package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.enums.TypeTransaction;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionsRepository;
import com.bbva.wallet.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private AccountRepository accountRepository;
    private TransactionsRepository transactionsRepository;
    public Transactions sendArs(TransactionDto transactionDto, User userLoggedIn){
        Optional<Account> incomeAccount = accountRepository.findById(transactionDto.getDestinationAccountId());
        List<Account> paymentAccounts = accountRepository.findByUserId(11L);
        var sourceAccount = paymentAccounts.stream().filter(account->account.getCurrency() == Currency.ARS).findFirst();
        if (!incomeAccount.isPresent() || paymentAccounts.isEmpty()){
            return null; //agregar manejos de errores
        }
        if (incomeAccount.get().getUser() == sourceAccount.get().getUser()){
            return null; // agregar manejo de errores
        }
        if (transactionDto.getAmount() > sourceAccount.get().getTransactionLimit()){
            return null; // agregar manejo de errores
        }
        if (transactionDto.getAmount() > sourceAccount.get().getBalance()){
            return null; // agregar manejo de errores
        }
        var income = Transactions.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.INCOME)
                .account(incomeAccount.get()).build();
        var payment = Transactions.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.PAYMENT)
                .account(sourceAccount.get()).build();
        transactionsRepository.save(income);
        transactionsRepository.save(payment);
        Double newBalanceIncome = incomeAccount.get().getBalance() + transactionDto.getAmount();
        Double newBalancePayment = sourceAccount.get().getBalance() - transactionDto.getAmount();
        incomeAccount.get().setBalance(newBalanceIncome);
        sourceAccount.get().setBalance(newBalancePayment);
        accountRepository.save(incomeAccount.get());
        accountRepository.save(sourceAccount.get());
        return new Transactions();
    }
}
package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.BalanceDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.FixedTermDepositsRepository;
import com.bbva.wallet.repositories.TransactionsRepository;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final Utils utils;
    private final TransactionsRepository transactionsRepository;
    private final FixedTermDepositsRepository fixedTermDepositsRepository;


    @Value("${transaction.limit.ars}")
    private Double transactionLimitArs;
    @Value("${transaction.limit.usd}")
    private Double transactionLimitUsd;
    @Value("${initial.balance}")
    private Double initialBalance;



    public Optional<Account> findById(Long Id) {
        return accountRepository.findById(Id);
    }
    @Override
    public Optional<Account> findByUserIdAndCurrency(Long id, Currency currency) {
        return accountRepository.findByUserIdAndCurrency(id, currency);
    }
    @Override
    public void saveAll(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }
    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account createAccount(Currency currency, User userLoggedIn) {
        Account newAccount = Account.builder()
                .currency(currency)
                .transactionLimit(currency.equals(Currency.ARS) ? transactionLimitArs : transactionLimitUsd)
                .balance(initialBalance)
                .user(userLoggedIn)
                .cbu(utils.generateRandomCbu())
                .build();
        accountRepository.save(newAccount);
        return newAccount;
    }

    @Override
    public void softDeleteByUserId (Long id){
        List<Account> accounts = accountRepository.findByUserId(id);
        accounts.forEach(account -> {
            account.setSoftDelete(true);
            accountRepository.save(account);
        });
    }
    @Override
    public Optional<BalanceDTO> getBalance(Long userId) {
        List<Account> accountList = accountRepository.findByUserId(userId);

        Optional<Account> optionalUsdAccount = accountList.stream().filter(account -> account.getCurrency().equals(Currency.USD)).findFirst();
        Optional<Account> optionalArsAccount = accountList.stream().filter(account -> account.getCurrency().equals(Currency.ARS)).findFirst();

        if (optionalUsdAccount.isPresent() && optionalArsAccount.isPresent()) {
            Account usdAccount = optionalUsdAccount.get();
            Account arsAccount = optionalArsAccount.get();

            List<Transaction> transactions = new ArrayList<>();
            transactions.addAll(transactionsRepository.findAllByAccount(usdAccount));
            transactions.addAll(transactionsRepository.findAllByAccount(arsAccount));

            List<FixedTermDeposits> fixedTermDeposits = fixedTermDepositsRepository.findAllByAccount(arsAccount);

            return Optional.of(BalanceDTO.builder()
                    .accountArs(arsAccount.getBalance())
                    .accountUsd(usdAccount.getBalance())
                    .history(transactions)
                    .fixedTerms(fixedTermDeposits)
                    .build());
        } else {
            return Optional.empty();
        }
    }
}

package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final Utils utils;

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
    public void save(Account sourceAccount){
        accountRepository.save(sourceAccount);
    }
}

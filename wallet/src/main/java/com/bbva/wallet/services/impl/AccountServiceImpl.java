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

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account createAccount(Currency currency, User userLoggedIn) {
        Account newAccount = Account.builder()
                .currency(currency)
                .transactionLimit( currency.equals(Currency.ARS) ? transactionLimitArs : transactionLimitUsd )
                .balance(initialBalance)
                .user(userLoggedIn)
                .cbu(utils.generateRandomCbu())
                .build();
        accountRepository.save(newAccount);
        return newAccount;
    }
}

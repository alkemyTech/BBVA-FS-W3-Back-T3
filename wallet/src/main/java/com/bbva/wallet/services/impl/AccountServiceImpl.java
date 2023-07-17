package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.BalanceResponseDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.FixedTermDepositsRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final Utils utils;
    private final TransactionRepository transactionRepository;
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
    public List<Account> findByUserId(Long userId){
        return accountRepository.findByUserId(userId);
    }

    @Override
    public void saveAll(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }
    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account createAccount(Currency currency, User userLoggedIn, Double... balance) {
        Account newAccount = Account.builder()
                .currency(currency)
                .transactionLimit(currency.equals(Currency.ARS) ? transactionLimitArs : transactionLimitUsd)
                .balance(Arrays.stream(balance).findFirst().orElse(initialBalance))
                .user(userLoggedIn)
                .cbu(utils.generateRandomCbu())
                .build();
        accountRepository.save(newAccount);
        return newAccount;
    }

    @Override
    public Account createAccount(Currency currency, User userLoggedIn) {
        return createAccount(currency, userLoggedIn, initialBalance);
    }

    public long count() {
        return accountRepository.count();
    }

    @Override
    public void softDeleteByUserId (Long id){
        List<Account> accounts = accountRepository.findByUserId(id);
        accounts.forEach(account -> {
            account.setSoftDelete(true);
            accountRepository.save(account);
        });
    }

    public Optional<Account> getAccountByUserIdAndCurrency(Long userId, Currency currency) {
        return accountRepository.findByUserIdAndCurrency(userId, currency);
    }

    @Override
    public void save(Account sourceAccount){
        accountRepository.save(sourceAccount);
    }

    @Override
    public BalanceResponseDTO getBalance(Long userId) {
        Optional<Account> optionalUsdAccount = accountRepository.findByUserIdAndCurrency(userId, Currency.USD);
        Optional<Account> optionalArsAccount = accountRepository.findByUserIdAndCurrency(userId, Currency.ARS);

        List<Transaction> transactions = new ArrayList<>();
        List<FixedTermDeposits> fixedTermDeposits = new ArrayList<>();
        Double balanceArs = 0.0;
        Double balanceUsd = 0.0;

        if(optionalUsdAccount.isPresent()) {
            Account usdAccount = optionalUsdAccount.get();
            balanceUsd = usdAccount.getBalance();
            transactions.addAll(transactionRepository.findAllByAccount(usdAccount));
        }

        if(optionalArsAccount.isPresent()) {
            Account arsAccount = optionalArsAccount.get();
            balanceArs = arsAccount.getBalance();
            transactions.addAll(transactionRepository.findAllByAccount(arsAccount));
            fixedTermDeposits = fixedTermDepositsRepository.findAllByAccount(arsAccount);
        }

        return BalanceResponseDTO.builder()
                    .accountArs(balanceArs)
                    .accountUsd(balanceUsd)
                    .history(transactions)
                    .fixedTerms(fixedTermDeposits)
                    .build();
    }
}

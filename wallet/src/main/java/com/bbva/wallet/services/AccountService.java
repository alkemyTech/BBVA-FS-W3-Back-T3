package com.bbva.wallet.services;

import com.bbva.wallet.dtos.BalanceDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> findById(Long Id);
    Optional<Account> findByUserIdAndCurrency(Long id, Currency currency);
    List<Account> findByUserId(Long userId);
    void saveAll(List<Account> accounts);
    void softDeleteByUserId(Long id);
    List<Account> getUserAccounts(Long userId);
    Account createAccount(Currency currency, User userLoggedIn, Double... initialBalance);
    Account createAccount(Currency currency, User userLoggedIn);
    void save(Account sourceAccount);
    Optional<Account> getAccountByUserIdAndCurrency(Long id, Currency currency);
    BalanceDTO getBalance(Long userId);
    long count();
  }
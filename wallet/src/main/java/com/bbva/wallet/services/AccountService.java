package com.bbva.wallet.services;

import com.bbva.wallet.dtos.BalanceDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    void save(Account sourceAccount);
    Optional<Account> getAccountByUserIdAndCurrency(Long id, Currency currency);
    Optional<BalanceDTO> getBalance(Long userId);
    long count();
    List<Account> findAll();
    Page<Account> getAllAccounts(Pageable pageable);
  }

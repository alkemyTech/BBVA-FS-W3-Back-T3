package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.enums.Currency;

import java.util.Optional;

public interface AccountService {
    void softDeleteByUserId(Long id);
    Optional<Account> getAccountByUserIdAndCurrency(Long id, Currency currency);
    void save(Account account);
}

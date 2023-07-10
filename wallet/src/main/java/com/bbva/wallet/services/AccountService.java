package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;

import java.util.List;
public interface AccountService {
    List<Account> getUserAccounts(Long userId);

    Account createAccount(Currency currency, User userLoggedIn);
}
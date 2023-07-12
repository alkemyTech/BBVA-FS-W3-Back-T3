package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;

import java.util.List;

public interface AccountService {
    void softDeleteByUserId(Long id);
    List<Account> getUserAccounts(Long userId);

}
package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    void softDeleteByUserId(Long id);
    List<Account> getUserAccounts(Long userId);
    Page<Account> getAllAccounts(Pageable pageable);

}
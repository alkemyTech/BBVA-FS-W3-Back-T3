package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public void softDeleteByUserId(Long id) {
        List<Account> accounts = accountRepository.findByUserId(id);
        accounts.forEach(account -> {
            account.setSoftDelete(true);
            accountRepository.save(account);
        });
    }
}

package com.bbva.wallet.services.impl;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }


    @Override
    public void softDeleteByUserId(Long id) {
        List<Account> accounts = accountRepository.findByUserId(id);
        accounts.forEach(account -> {
            account.setSoftDelete(true);
            accountRepository.save(account);
        });
    }
}

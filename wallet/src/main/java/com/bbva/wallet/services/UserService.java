package com.bbva.wallet.services;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bbva.wallet.repositories.UserRepository;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public List<Account> getUserAccounts(Long userId) {
        var user = userRepository.findById(userId);
        List<Account> accounts = new ArrayList<>();
        if (user.isPresent()) {
            accounts = accountRepository.findByUser(user.get());
        }
        return accounts;
    }
}
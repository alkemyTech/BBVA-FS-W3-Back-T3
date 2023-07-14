package com.bbva.wallet.seeder;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.UserService;
import com.bbva.wallet.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountSeeder {
    private final AccountService accountService;
    private final UserService userService;
    private final UserSeeder userSeeder;
    private final Utils utils;
    @PostConstruct
    public void seedAccounts() {

       List<User> users =  userSeeder.seedUsers();

        if (accountService.count() != 0) {
            return;
        }
        // If there are users but no accounts, create accounts for all users
        if (users.isEmpty()) {
            users = userService.getAll();
        }
        users.forEach(user -> {
            Account accountArs, accountUsd;
            if(user.getId() % 2 == 0) {
                accountArs = accountService.createAccount(Currency.ARS, user, utils.generateRandomInitialBalance());
            }
            if (user.getId() % 2 != 0 || user.getFirstName().equals("Nicolás")) {
                accountUsd = accountService.createAccount(Currency.USD, user, utils.generateRandomInitialBalance());
            }
        });
    }
}

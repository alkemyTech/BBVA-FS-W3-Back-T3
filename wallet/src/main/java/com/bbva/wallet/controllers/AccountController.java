package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        Long user_id = 0L;
        Currency currency = accountDTO.getCurrency();
        var userAccounts = accountService.getUserAccounts(user_id);

        if (userAccounts.stream().anyMatch(
                account -> account.getCurrency().equals(currency)) ) {
            return ResponseEntity.badRequest().build();
        }

        Account newAccount =  Account.builder()
                .currency(currency)
                .transactionLimit( currency.equals(Currency.ARS) ? 300000.0 : 1000.0 )
                .balance(0.0)
                .user(userService.getUserById(user_id))
                .cbu(utils.generateRandomCbu())
                .build();
        return ResponseEntity.ok(newAccount);
    }
}
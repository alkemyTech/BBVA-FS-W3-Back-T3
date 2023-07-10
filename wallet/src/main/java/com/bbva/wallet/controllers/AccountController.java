package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final Utils utils;
    @Value("${transaction.limit.ars}")
    private Double transactionLimitArs;
    @Value("${transaction.limit.usd}")
    private Double transactionLimitUsd;
    @Value("${initial.balance}")
    private Double initialBalance;

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDTO, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Long user_id = userLoggedIn.getId();
        Currency currency = accountDTO.getCurrency();

        var userAccounts = accountService.getUserAccounts(user_id);

        if (userAccounts.stream().anyMatch(
                account -> account.getCurrency().equals(currency)) ) {
            return ResponseEntity.badRequest().build();
        }

        Account newAccount =  Account.builder()
                .currency(currency)
                .transactionLimit( currency.equals(Currency.ARS) ? transactionLimitArs : transactionLimitUsd )
                .balance(initialBalance)
                .user(userLoggedIn)
                .cbu(utils.generateRandomCbu())
                .build();
        return ResponseEntity.ok(newAccount);
    }
}
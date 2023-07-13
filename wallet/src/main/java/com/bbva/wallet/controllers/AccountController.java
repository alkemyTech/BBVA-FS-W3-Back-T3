package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{userId}")
    public ResponseEntity<Iterable<Account>> getUserAccounts(@PathVariable Long userId) {
        Iterable<Account> entities = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(entities);
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO accountDTO, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Long user_id = userLoggedIn.getId();

        Currency dtoCurrency = accountDTO.getCurrency();

        var userAccounts = accountService.getUserAccounts(user_id);

        if (userAccounts.stream().anyMatch(
                account -> account.getCurrency().equals(dtoCurrency)) ) {
            throw new AccountException("El usuario ya tiene una cuenta en esa moneda", ErrorCodes.ACCOUNT_ALREADY_EXISTS);
        }
        return ResponseEntity.ok(accountService.createAccount(dtoCurrency, userLoggedIn));
    }
}
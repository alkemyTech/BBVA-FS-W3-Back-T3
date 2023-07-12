package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountTransactionLimitDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
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
        Iterable<Account> entities =accountService.getUserAccounts(userId);
        return ResponseEntity.ok(entities);
    }

    @SneakyThrows
    @PatchMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Long id, @RequestBody AccountTransactionLimitDto accountTransactionLimitDto, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account account= accountService.findById(id).orElseThrow(() -> new TransactionException("No existe la cuenta indicada ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        if (account.getUser().getId()!=userLoggedIn.getId()){
            throw new AccountException("No se puede modificar una cuenta ajena ", ErrorCodes.ACCOUNT_DOESNT_EXIST);
        }

        account.setTransactionLimit(accountTransactionLimitDto.getTransactionLimit());
        accountService.save(account);
        return ResponseEntity.ok(account);

    }
}
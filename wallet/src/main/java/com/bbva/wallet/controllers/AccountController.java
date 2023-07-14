package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.AccountDTO;
import com.bbva.wallet.dtos.BalanceDTO;
import com.bbva.wallet.dtos.AccountTransactionLimitDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

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

    @GetMapping("/balance")
    public ResponseEntity<Optional<BalanceDTO>> getBalance(Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Optional<BalanceDTO> balanceDTO = accountService.getBalance(userLoggedIn.getId());
        return ResponseEntity.ok(balanceDTO);
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
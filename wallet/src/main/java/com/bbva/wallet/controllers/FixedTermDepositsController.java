package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.FixedTermDepositsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fixedTerm")
@RequiredArgsConstructor
public class FixedTermDepositsController {
    private final FixedTermDepositsService fixedTermDepositsService;
    private final AccountService accountService;
    @Value("${fixedTermDeposit.currency}")
    private Currency currency;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<FixedTermDeposits> createFixedTermDeposit(@Valid @RequestBody FixedTermDepositsDTO fixedTermDepositsDTO,
    Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();

        Account account =
                accountService.getAccountByUserIdAndCurrency(userLoggedIn.getId(), currency).orElseThrow(
                        () -> new AccountException("El usuario no tiene account en esta moneda: " + currency,
                                ErrorCodes.ACCOUNT_DOESNT_EXIST));

        FixedTermDeposits fixedTermDeposits = fixedTermDepositsService.createFixedTermDeposit(
                fixedTermDepositsDTO,
                account);
        //save con el nuevo balance
        accountService.save(account);

        return ResponseEntity.ok().body(fixedTermDeposits);
    }
}

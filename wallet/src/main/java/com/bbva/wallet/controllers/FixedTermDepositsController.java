package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.FixedTermDepositsRequestDTO;
import com.bbva.wallet.dtos.FixedTermDepositsResponseDTO;
import com.bbva.wallet.entities.Account;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<FixedTermDepositsResponseDTO> createFixedTermDeposit(@Valid @RequestBody FixedTermDepositsRequestDTO fixedTermDepositsRequestDTO,
    Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();

        Account account =
                accountService.getAccountByUserIdAndCurrency(userLoggedIn.getId(), currency).orElseThrow(
                        () -> new AccountException("El usuario no tiene account en esta moneda: " + currency,
                                ErrorCodes.ACCOUNT_DOESNT_EXIST));

        FixedTermDepositsResponseDTO fixedTermDeposits = fixedTermDepositsService.createFixedTermDeposit(
                fixedTermDepositsRequestDTO,
                account);

        accountService.save(account);

        return ResponseEntity.ok().body(fixedTermDeposits);
    }

    @SneakyThrows
    @PostMapping("/simulate")
    public ResponseEntity<FixedTermDepositsResponseDTO> simulateFixedTermDeposit(@Valid @RequestBody FixedTermDepositsRequestDTO fixedTermDepositsRequestDTO) {
        FixedTermDepositsResponseDTO simulation = fixedTermDepositsService.createFixedTermDeposit(fixedTermDepositsRequestDTO);
        return ResponseEntity.ok().body(simulation);
    }
}

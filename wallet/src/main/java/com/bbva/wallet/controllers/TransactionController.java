package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping("/sendArs")
    public ResponseEntity <Transactions> sendArs(@RequestBody TransactionDto transactionDto) {
        // User userLoggedIn = (User) authentication.getPrincipal();
        Account sourceAccount = accountService.findByUserIdAndCurrency(11L, Currency.ARS).orElse(null); //TODO: agregar excepcion si no tiene cuenta en ARS
        Account destinationAccount = accountService.findById(transactionDto.getDestinationAccountId()).orElse(null); //TODO: Agregar excepcion si no existe la cuenta

        transactionService.sendArs(transactionDto, sourceAccount, destinationAccount);
        accountService.saveAll(List.of(sourceAccount, destinationAccount));
        return ResponseEntity.ok().build();
    }
}
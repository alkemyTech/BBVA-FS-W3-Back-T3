package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;


    @PostMapping("/sendArs")
    public ResponseEntity <Void> sendArs(@RequestBody @Valid TransactionDto transactionDto,Authentication authentication) {
        return send(transactionDto,Currency.ARS, authentication);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity <Void> sendUsd(@RequestBody @Valid TransactionDto transactionDto, Authentication authentication) {
        return send(transactionDto,Currency.USD, authentication);
    }

    //@PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.getId ")
    @GetMapping
    public ResponseEntity<Iterable<Transaction>> getUserAccounts( Authentication authentication) {//TODO: Login con id variable
        User userLoggedIn = (User) authentication.getPrincipal();
        List<Account> accounts = accountService.findByUserId(userLoggedIn.getId());
        Iterable<Transaction> transactions = transactionService.getUserTransaction(accounts);
        return ResponseEntity.ok(transactions);
    }


    private ResponseEntity <Void> send(@RequestBody TransactionDto transactionDto,Currency currency,Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account sourceAccount = accountService.findByUserIdAndCurrency(userLoggedIn.getId(), currency).orElse(null); //TODO: agregar excepcion si no tiene cuenta en ARS
        Account destinationAccount = accountService.findById(transactionDto.getDestinationAccountId()).orElse(null); //TODO: Agregar excepcion si no existe la cuenta

        transactionService.send(transactionDto, sourceAccount, destinationAccount);
        accountService.saveAll(List.of(sourceAccount, destinationAccount));
        return ResponseEntity.ok().build();
    }
}
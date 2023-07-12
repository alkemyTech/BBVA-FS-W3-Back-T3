package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDescriptionDto;
import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.TransactionalException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping("/sendArs")
    public ResponseEntity<Transaction> sendArs(@RequestBody @Valid TransactionDto transactionDto, Authentication authentication) {
        return send(transactionDto, Currency.ARS, authentication);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<Transaction> sendUsd(@RequestBody @Valid TransactionDto transactionDto, Authentication authentication) {
        return send(transactionDto, Currency.USD, authentication);
    }

    @SneakyThrows
    private ResponseEntity<Transaction> send(@RequestBody TransactionDto transactionDto, Currency currency,
                                             Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account sourceAccount = accountService.findByUserIdAndCurrency(userLoggedIn.getId(), currency).orElseThrow(() -> new AccountException("El usuario no posee una cuenta en " + currency, ErrorCodes.ACCOUNT_DOESNT_EXIST));
        Account destinationAccount = accountService.findById(transactionDto.getDestinationAccountId()).orElseThrow(() -> new AccountException("La cuenta destino no existe ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        Transaction payment = transactionService.send(transactionDto, sourceAccount, destinationAccount);
        accountService.saveAll(List.of(sourceAccount, destinationAccount));
        return ResponseEntity.ok(payment);
    }

    @SneakyThrows
    @PatchMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") Long id, @RequestBody TransactionDescriptionDto transactionDescriptionDto, Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Transaction transaction= transactionService.findById(id).orElseThrow(() -> new TransactionException("No existe la transaccion indicada ", ErrorCodes.TRANSACTION_DOESNT_EXIST));
        Account sourceAccount = accountService.findById(transaction.getAccount().getId()).orElseThrow(() -> new AccountException("No existe la cuenta ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        if (sourceAccount.getUser().getId()!=userLoggedIn.getId()){
           throw new TransactionException("No se puede modificar una transaccion ajena ", ErrorCodes.TRANSACTION_DOESNT_EXIST);
        }

        transaction.setDescription(transactionDescriptionDto.getDescription());
        transactionService.save(transaction);

       return ResponseEntity.ok(transaction);

    }

}
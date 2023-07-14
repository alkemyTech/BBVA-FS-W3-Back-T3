package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.AccountException;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.TransactionService;
import com.bbva.wallet.utils.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final Utils utils;

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

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.getId")
    @GetMapping("/{id}")
    public ResponseEntity<PagedModel<EntityModel<Transaction>>> getUserTransactions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<Transaction> pagedAssembler) {

        List<Account> accounts = accountService.findByUserId(id);
        Iterable<Transaction> transactions = transactionService.getUserTransaction(accounts);

        Page<Transaction> transactionPage = utils.paginateTransactions(transactions, page, size);

        PagedModel<EntityModel<Transaction>> pagedModel = pagedAssembler.toModel(transactionPage);
        return ResponseEntity.ok(pagedModel);
    }
}
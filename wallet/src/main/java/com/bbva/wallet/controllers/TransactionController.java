package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.DepositCreatedDTO;
import com.bbva.wallet.dtos.DepositDTO;
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
    public ResponseEntity <Transaction> sendArs(@RequestBody @Valid TransactionDto transactionDto, Authentication authentication) {
        return send(transactionDto,Currency.ARS, authentication);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity<Transaction> sendUsd(@RequestBody @Valid TransactionDto transactionDto, Authentication authentication) {
        return send(transactionDto,Currency.USD, authentication);
    }
    @SneakyThrows
    private ResponseEntity<Transaction> send(@RequestBody TransactionDto transactionDto, Currency currency,
                                             Authentication authentication) {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account sourceAccount = accountService.findByUserIdAndCurrency(userLoggedIn.getId(), currency).orElseThrow(() -> new AccountException("El usuario no posee una cuenta en "+currency, ErrorCodes.ACCOUNT_DOESNT_EXIST));
        Account destinationAccount = accountService.findById(transactionDto.getDestinationAccountId()).orElseThrow(() -> new AccountException("La cuenta destino no existe ", ErrorCodes.ACCOUNT_DOESNT_EXIST));

        Transaction payment = transactionService.send(transactionDto, sourceAccount, destinationAccount);
        accountService.saveAll(List.of(sourceAccount, destinationAccount));
        return ResponseEntity.ok(payment);
    }

    //------------------------------------------Deposit--------------------------------------------------------------
    @PostMapping("/deposit")
    public ResponseEntity<DepositCreatedDTO> deposit(@RequestBody DepositDTO depositDTO, Authentication authentication) throws AccountException, TransactionException {
        User userLoggedIn = (User) authentication.getPrincipal();
        Account sourceAccount = accountService.findByUserIdAndCurrency(userLoggedIn.getId(), depositDTO.getCurrency())
                .orElseThrow(() -> new AccountException("El usuario no posee una cuenta en " + depositDTO.getCurrency(), ErrorCodes.ACCOUNT_DOESNT_EXIST));

        accountService.save(sourceAccount);

        DepositCreatedDTO depositCreatedDTO = transactionService.deposit(sourceAccount, depositDTO.getAmount());
        return ResponseEntity.ok(depositCreatedDTO);
    }
}
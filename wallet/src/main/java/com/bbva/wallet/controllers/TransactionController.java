package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/sendArs")
    public ResponseEntity <Transactions> sendArs(@RequestBody TransactionDto transactionDto) {
       // User userLoggedIn = (User) authentication.getPrincipal();
        User userLoggedIn = new User();
        var transactions = transactionService.sendArs(transactionDto, userLoggedIn);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/sendUsd")
    public ResponseEntity <Transactions> sendUsd(@RequestBody TransactionDto transactionDto) {
        // User userLoggedIn = (User) authentication.getPrincipal();
        User userLoggedIn = new User();
        var transactions = transactionService.sendUsd(transactionDto, userLoggedIn);
        return ResponseEntity.ok(transactions);
    }
}
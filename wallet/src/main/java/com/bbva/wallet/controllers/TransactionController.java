package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Transactions;
import com.bbva.wallet.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")

public class TransactionController {
    private TransactionService transactionService;

    @PostMapping("/sendArs")
    public ResponseEntity <Transactions> sendArs(@RequestBody TransactionDto transactionDto) {
        var transactions = transactionService.sendArs(transactionDto);
        return ResponseEntity.ok(transactions);
    }
}
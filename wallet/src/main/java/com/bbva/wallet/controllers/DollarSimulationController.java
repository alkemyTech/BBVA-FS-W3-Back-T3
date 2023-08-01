package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.DollarSimulationDTO;
import com.bbva.wallet.dtos.DollarSimulationResultDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.Currency;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.AccountService;
import com.bbva.wallet.services.DollarSimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trading")
@RequiredArgsConstructor
public class DollarSimulationController {
    private final DollarSimulationService dollarSimulationService;
    private final AccountService accountService;

    @PostMapping("/simulateDollarPurchase")
    public ResponseEntity<DollarSimulationResultDTO> simulateDollarTransaction(@RequestBody @Valid DollarSimulationDTO dto) {
        DollarSimulationResultDTO resultDTO = dollarSimulationService.simulateDollarTransaction(dto);
        return ResponseEntity.ok(resultDTO);
    }

    @SneakyThrows
    @PostMapping("/dollarPurchase")
    public ResponseEntity<DollarSimulationResultDTO> dollarTransaction(@RequestBody @Valid DollarSimulationDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Account accountArs = accountService.getAccountByUserIdAndCurrency(user.getId(), Currency.ARS)
                .orElseThrow(
                        () -> new TransactionException("Account not found", ErrorCodes.ACCOUNT_NOT_FOUND)
                );
        Account accountUsd = accountService.getAccountByUserIdAndCurrency(user.getId(), Currency.USD)
                .orElseThrow(
                        () -> new TransactionException("Account not found", ErrorCodes.ACCOUNT_NOT_FOUND)
                );

        DollarSimulationResultDTO resultDTO = dollarSimulationService.dollarTransaction(dto, accountArs, accountUsd);
        return ResponseEntity.ok(resultDTO);
    }
}

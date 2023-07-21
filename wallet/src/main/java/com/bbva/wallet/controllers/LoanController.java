package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanRequestDTO;
import com.bbva.wallet.dtos.LoanResponseDTO;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Loan")
@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @Operation(summary = "Crear un prestamo",description = "Crear un prestamo")
    @SneakyThrows
    @PostMapping("/simulate")
    public ResponseEntity<LoanResponseDTO> simulateLoan(@RequestBody @Valid LoanRequestDTO loanRequestDTO) {
   if(loanRequestDTO.getTerm() <= 0){
            throw new TransactionException("La cantidad de meses no debe ser 0 ni debe estar vacio", ErrorCodes.INVALID_AMOUNT);
        }
        double amount = loanRequestDTO.getAmount();
        int term = loanRequestDTO.getTerm();
        LoanResponseDTO loanResponseDTO = loanService.simulate(amount, term);
        return ResponseEntity.ok(loanResponseDTO);
    }
}
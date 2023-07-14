package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.LoanRequestDTO;
import com.bbva.wallet.dtos.LoanResponseDTO;
import com.bbva.wallet.entities.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {

    @PostMapping("/simulate")
    public ResponseEntity<LoanResponseDTO> simulateLoan(@RequestBody LoanRequestDTO loanRequestDTO) {
        return null;
    }
}

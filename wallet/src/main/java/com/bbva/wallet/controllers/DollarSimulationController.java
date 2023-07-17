package com.bbva.wallet.controllers;

import com.bbva.wallet.dtos.DollarSimulationDTO;
import com.bbva.wallet.dtos.DollarSimulationResultDTO;
import com.bbva.wallet.services.DollarSimulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trading")
@RequiredArgsConstructor
public class DollarSimulationController {

    private final DollarSimulationService dollarSimulationService;

    @PostMapping("/simulateUsdToArs")
    public ResponseEntity<DollarSimulationResultDTO> simulateDollarTransaction(@RequestBody @Valid DollarSimulationDTO dto) {
        DollarSimulationResultDTO resultDTO = dollarSimulationService.simulateDollarTransaction(dto);
        return ResponseEntity.ok(resultDTO);
    }
}

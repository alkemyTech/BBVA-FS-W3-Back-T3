package com.bbva.wallet.services;

import com.bbva.wallet.dtos.DollarSimulationDTO;
import com.bbva.wallet.dtos.DollarSimulationResultDTO;

public interface DollarSimulationService {
    DollarSimulationResultDTO simulateDollarTransaction(DollarSimulationDTO dto);
}
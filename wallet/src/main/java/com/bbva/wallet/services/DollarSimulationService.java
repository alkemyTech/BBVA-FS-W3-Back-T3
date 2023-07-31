package com.bbva.wallet.services;

import com.bbva.wallet.dtos.DollarSimulationDTO;
import com.bbva.wallet.dtos.DollarSimulationResultDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exeptions.TransactionException;

public interface DollarSimulationService {
    DollarSimulationResultDTO simulateDollarTransaction(DollarSimulationDTO dto);
    DollarSimulationResultDTO dollarTransaction(DollarSimulationDTO dto, Account accountArs, Account accountUsd) throws TransactionException;
}
package com.bbva.wallet.services;

import com.bbva.wallet.dtos.FixedTermDepositsRequestDTO;
import com.bbva.wallet.dtos.FixedTermDepositsResponseDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exeptions.FixedTermDepositsException;

public interface FixedTermDepositsService {
    FixedTermDepositsResponseDTO createFixedTermDeposit(FixedTermDepositsRequestDTO fixedTermDepositsRequestDTO, Account account) throws FixedTermDepositsException;

    FixedTermDepositsResponseDTO createFixedTermDeposit(FixedTermDepositsRequestDTO fixedTermDepositsRequestDTO) throws FixedTermDepositsException;
}

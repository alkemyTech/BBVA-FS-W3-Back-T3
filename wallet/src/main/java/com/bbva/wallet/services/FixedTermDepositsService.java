package com.bbva.wallet.services;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.exeptions.FixedTermDepositsException;

public interface FixedTermDepositsService {
    void createFixedTermDeposit(FixedTermDepositsDTO fixedTermDepositsDTO, Account account) throws FixedTermDepositsException;
}

package com.bbva.wallet.services;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.entities.Account;

public interface FixedTermDepositsService {
    void createFixedTermDeposit(FixedTermDepositsDTO fixedTermDepositsDTO, Account account);
}

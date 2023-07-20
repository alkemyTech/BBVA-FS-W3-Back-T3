package com.bbva.wallet.services;

import com.bbva.wallet.dtos.LoanResponseDTO;

public interface LoanService {

    LoanResponseDTO simulate(Double amount, Integer month);
}

package com.bbva.wallet.services;

import com.bbva.wallet.dtos.LoanResponseDTO;
import org.springframework.stereotype.Service;


public interface LoanService {

    LoanResponseDTO simulate(Double amount, Integer month);
}
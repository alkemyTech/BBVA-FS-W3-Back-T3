package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.LoanResponseDTO;
import com.bbva.wallet.services.LoanService;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {
    @Override
    public LoanResponseDTO simulate(double amount, Integer month) {
        return null;
    }
}

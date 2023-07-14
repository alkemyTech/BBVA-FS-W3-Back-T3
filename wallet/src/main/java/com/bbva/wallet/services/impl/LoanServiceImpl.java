package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.LoanResponseDTO;
import com.bbva.wallet.services.LoanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanServiceImpl implements LoanService {
    @Value("${loan.interest-rate}")
    private Double interestRate;
    @Override
    public LoanResponseDTO simulate(double amount, Integer term) {
        Double interest = amount * interestRate * term;
        Double totalPayment = amount + interest;
        Double monthlyPayment = totalPayment / term;
        return LoanResponseDTO.builder()
                .monthlyPayment(roundDecimalValue(monthlyPayment, 2))
                .totalPayment(roundDecimalValue(totalPayment, 2))
                .interestRate(interestRate)
                .build();
    }
    private Double roundDecimalValue(Double value, Integer decimalPlaces) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
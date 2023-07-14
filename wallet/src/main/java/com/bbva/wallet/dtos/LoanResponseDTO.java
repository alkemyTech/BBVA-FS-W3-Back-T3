package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoanResponseDTO {
    @NotNull
    private double monthlyFee;

    @NotNull
    private double totalPayment;

    @NotNull
    private double interesRate;

}

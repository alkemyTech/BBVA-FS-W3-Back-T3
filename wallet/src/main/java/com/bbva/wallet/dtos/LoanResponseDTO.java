package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponseDTO {
    @NotNull
    private Double monthlyPayment;

    @NotNull
    private Double totalPayment;

    @NotNull
    private Double interestRate;
}

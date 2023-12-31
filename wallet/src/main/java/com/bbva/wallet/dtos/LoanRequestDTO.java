package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@AllArgsConstructor
public class LoanRequestDTO {
    @NotNull
    private Double amount;

    @NotNull
    private Integer term;
}
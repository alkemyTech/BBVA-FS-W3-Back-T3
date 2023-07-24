package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class FixedTermDepositsRequestDTO {
    @NotNull
    private Double amount;

    @NotNull
    private LocalDate closingDate;
}

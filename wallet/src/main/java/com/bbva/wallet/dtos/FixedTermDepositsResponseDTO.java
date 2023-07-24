package com.bbva.wallet.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class FixedTermDepositsResponseDTO {
    private Double amount;
    private Double interest;
    private Double total;
    private LocalDate closingDate;
    private LocalDate creationDate;
}

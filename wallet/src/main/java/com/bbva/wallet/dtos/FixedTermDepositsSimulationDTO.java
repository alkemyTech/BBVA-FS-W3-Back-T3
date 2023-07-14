package com.bbva.wallet.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class FixedTermDepositsSimulationDTO {
    private Double amount;
    private Double interest;
    private Double total;
    private LocalDateTime closingDate;
    private LocalDateTime creationDate;
}

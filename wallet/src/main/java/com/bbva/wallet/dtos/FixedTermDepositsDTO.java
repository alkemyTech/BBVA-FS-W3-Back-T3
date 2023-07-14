package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.Timestamp;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@Data
public class FixedTermDepositsDTO {
    @NotNull
    private Double amount;

    @NotNull
    @Timestamp
    private LocalDateTime closingDate;
}

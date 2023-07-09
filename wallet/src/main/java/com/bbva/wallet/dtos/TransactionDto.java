package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @NotNull
    private Long destinationAccountId;
    @NotNull
    private Double amount;
    @NotNull
    private Long sourceAccountId;
    @NotNull
    private LocalDateTime transactionDate;
}
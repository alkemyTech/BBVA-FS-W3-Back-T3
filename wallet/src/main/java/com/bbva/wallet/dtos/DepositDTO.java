package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositDTO {
    @NotNull
    private Double amount;
    @NotNull
    private Currency currency;
}

package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.DollarType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DollarSimulationDTO {

    @NotNull
    @Positive
    private Double amountInPesos;

    @NotNull
    private DollarType dollarType;
    public @NotNull DollarType getDollarType() {
        return dollarType;
    }
}
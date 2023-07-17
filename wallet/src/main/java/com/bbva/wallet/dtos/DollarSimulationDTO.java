package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.DollarType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DollarSimulationDTO {
    private double amountInPesos;
    private DollarType dollarType;
    public DollarType getDollarType() {
        return dollarType;
    }
}
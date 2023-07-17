package com.bbva.wallet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DollarSimulationResultDTO {
    private double dollarValue;
    private double dollarValueWithTax;
    private String lastUpdate;
    private double impuestoPais;
    private double retencionGanancias;
    private double amountInDollars;
}

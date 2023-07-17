package com.bbva.wallet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DollarInfoDTO {
    private double officialDollarValue;
    private String lastUpdate;


}
package com.bbva.wallet.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficialDollar {
    @JsonProperty("value_sell")
    private double valueSell;


}
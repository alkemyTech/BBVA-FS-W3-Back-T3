package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.OfficialDollar;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class DollarApiResponse {
    @JsonProperty("oficial")
    private OfficialDollar official;

    @JsonProperty("last_update")
    private String lastUpdate;


}
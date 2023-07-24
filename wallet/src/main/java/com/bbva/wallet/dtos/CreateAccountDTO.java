package com.bbva.wallet.dtos;

import com.bbva.wallet.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountDTO {

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

}
package com.bbva.wallet.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDescriptionDto {

    @NotNull
    private String description;

}

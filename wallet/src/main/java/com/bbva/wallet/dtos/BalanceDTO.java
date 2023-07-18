package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.entities.Transaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class BalanceDTO {
    private Double accountArs;
    private Double accountUsd;
    private List<Transaction> history;
    private List<FixedTermDeposits> fixedTerms;
}
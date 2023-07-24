package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.dtos.FixedTermDepositsSimulationDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.FixedTermDepositsException;
import com.bbva.wallet.repositories.FixedTermDepositsRepository;
import com.bbva.wallet.services.FixedTermDepositsService;
import com.bbva.wallet.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FixedTermDepositsServiceImpl implements FixedTermDepositsService {
    private final FixedTermDepositsRepository fixedTermDepositsRepository;
    private final Utils utils;

    @Value("${fixedTermDeposit.dailyInterestRate}")
    private Double dailyInterestRate;
    @Override
    public FixedTermDeposits createFixedTermDeposit(FixedTermDepositsDTO fixedTermDepositsDTO, Account account) throws FixedTermDepositsException {
        if (account.getBalance() <= fixedTermDepositsDTO.getAmount()) {
            throw new FixedTermDepositsException("Insufficient funds", ErrorCodes.INSUFFICIENT_FUNDS);
        }
        if (LocalDateTime.now().plusDays(30L).isAfter(fixedTermDepositsDTO.getClosingDate().atStartOfDay())) {
            throw new FixedTermDepositsException("La fecha de cierre debe ser al menos 30 días a partir de ahora.", ErrorCodes.INVALID_CLOSING_DATE);
        }

        FixedTermDeposits fixedTermDeposits = FixedTermDeposits.builder()
                .amount(fixedTermDepositsDTO.getAmount())
                .account(account)
                .interest(dailyInterestRate)
                .closingDate(fixedTermDepositsDTO.getClosingDate().atStartOfDay())
                .build();

        account.setBalance(account.getBalance() - fixedTermDepositsDTO.getAmount());

        fixedTermDepositsRepository.save(fixedTermDeposits);
        return fixedTermDeposits;
    }

    public FixedTermDepositsSimulationDTO createFixedTermDeposit(FixedTermDepositsDTO fixedTermDepositsDTO) throws FixedTermDepositsException{
        if (LocalDateTime.now().plusDays(30L).isAfter(fixedTermDepositsDTO.getClosingDate().atStartOfDay())) {
            throw new FixedTermDepositsException("La fecha de cierre debe ser al menos 30 días a partir de ahora.", ErrorCodes.INVALID_CLOSING_DATE);
        }
        return FixedTermDepositsSimulationDTO.builder()
                .amount(fixedTermDepositsDTO.getAmount())
                .interest(dailyInterestRate)
                .closingDate(fixedTermDepositsDTO.getClosingDate().atStartOfDay())
                .creationDate(LocalDateTime.now())
                .total(
                        utils.calculateTotal(
                            fixedTermDepositsDTO.getAmount(),
                                dailyInterestRate,
                                fixedTermDepositsDTO.getClosingDate().atStartOfDay(),
                                LocalDateTime.now()
                        )
                )
                .build();
    }
}
package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.FixedTermDepositsException;
import com.bbva.wallet.repositories.FixedTermDepositsRepository;
import com.bbva.wallet.services.FixedTermDepositsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FixedTermDepositsServiceImpl implements FixedTermDepositsService {
    private final FixedTermDepositsRepository fixedTermDepositsRepository;

    @Value("${fixedTermDeposit.dailyInterestRate}")
    private Double dailyInterestRate;
    @Override
    public FixedTermDeposits createFixedTermDeposit(FixedTermDepositsDTO fixedTermDepositsDTO, Account account) throws FixedTermDepositsException {
        if (account.getBalance() <= fixedTermDepositsDTO.getAmount()) {
            throw new FixedTermDepositsException("Insufficient funds", ErrorCodes.INSUFFICIENT_FUNDS); //todo: create a custom exception
        }
        if (LocalDateTime.now().plusDays(30L).isAfter(fixedTermDepositsDTO.getClosingDate())) {
            throw new FixedTermDepositsException("The closing date must be at least 30 days from now", ErrorCodes.INVALID_CLOSING_DATE);
        }

        FixedTermDeposits fixedTermDeposits = FixedTermDeposits.builder()
                .amount(fixedTermDepositsDTO.getAmount())
                .account(account)
                .interest(dailyInterestRate)
                .closingDate(fixedTermDepositsDTO.getClosingDate())
                .build();

        account.setBalance(account.getBalance() - fixedTermDepositsDTO.getAmount());

        fixedTermDepositsRepository.save(fixedTermDeposits);
        return fixedTermDeposits;
    }
}
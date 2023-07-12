package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.FixedTermDepositsDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.FixedTermDeposits;
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
    public void createFixedTermDeposit(FixedTermDepositsDTO fixedTermDepositsDTO, Account account) {
        if (account.getBalance() <= fixedTermDepositsDTO.getAmount()) {
            throw new RuntimeException("Insufficient funds"); //todo: create a custom exception
        }
        if (LocalDateTime.now().plusDays(30L).isAfter(fixedTermDepositsDTO.getClosingDate())) {
            throw new RuntimeException("The closing date must be at least 30 days from now"); //todo: create a custom exception
        }

        FixedTermDeposits fixedTermDeposits = FixedTermDeposits.builder()
                .amount(fixedTermDepositsDTO.getAmount())
                .account(account)
                .interest(dailyInterestRate)
                .closingDate(fixedTermDepositsDTO.getClosingDate())
                .build();

        account.setBalance(account.getBalance() - fixedTermDepositsDTO.getAmount());

        fixedTermDepositsRepository.save(fixedTermDeposits);
    }
}
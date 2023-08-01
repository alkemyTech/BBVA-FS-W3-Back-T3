package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.FixedTermDepositsRequestDTO;
import com.bbva.wallet.dtos.FixedTermDepositsResponseDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FixedTermDepositsServiceImpl implements FixedTermDepositsService {
    private final FixedTermDepositsRepository fixedTermDepositsRepository;
    private final Utils utils;

    @Value("${fixedTermDeposit.dailyInterestRate}")
    private Double dailyInterestRate;
    @Override
    public FixedTermDepositsResponseDTO createFixedTermDeposit(FixedTermDepositsRequestDTO fixedTermDepositsRequestDTO, Account account) throws FixedTermDepositsException {
        if (account.getBalance() <= fixedTermDepositsRequestDTO.getAmount()) {
            throw new FixedTermDepositsException("Fondos en pesos insuficientes", ErrorCodes.INSUFFICIENT_FUNDS);
        }
        FixedTermDepositsResponseDTO simulation = createFixedTermDeposit(fixedTermDepositsRequestDTO);

        FixedTermDeposits fixedTermDeposits = FixedTermDeposits.builder()
                .amount(fixedTermDepositsRequestDTO.getAmount())
                .account(account)
                .interest(dailyInterestRate)
                .closingDate(fixedTermDepositsRequestDTO.getClosingDate().atStartOfDay())
                .build();

        account.setBalance(account.getBalance() - fixedTermDepositsRequestDTO.getAmount());

        fixedTermDepositsRepository.save(fixedTermDeposits);
        return simulation;
    }

    public FixedTermDepositsResponseDTO createFixedTermDeposit(FixedTermDepositsRequestDTO fixedTermDepositsRequestDTO) throws FixedTermDepositsException{
        if (LocalDateTime.now().plusDays(30L).isAfter(fixedTermDepositsRequestDTO.getClosingDate().atStartOfDay())) {
            throw new FixedTermDepositsException("La fecha de cierre debe ser al menos 30 días a partir de ahora.", ErrorCodes.INVALID_CLOSING_DATE);
        }
        return FixedTermDepositsResponseDTO.builder()
                .amount(fixedTermDepositsRequestDTO.getAmount())
                .interest(dailyInterestRate)
                .closingDate(LocalDate.from(fixedTermDepositsRequestDTO.getClosingDate()))
                .creationDate(LocalDate.now())
                .total(
                        utils.calculateTotal(
                            fixedTermDepositsRequestDTO.getAmount(),
                                dailyInterestRate,
                                fixedTermDepositsRequestDTO.getClosingDate().atStartOfDay(),
                                LocalDateTime.now()
                        )
                )
                .build();
    }

    @Override
    public FixedTermDepositsResponseDTO cancelFixedTerm(Long id) throws FixedTermDepositsException{
        FixedTermDeposits fixedTermDeposits = fixedTermDepositsRepository.findById(id).orElseThrow(
                () -> new FixedTermDepositsException("No se encontró el plazo fijo", ErrorCodes.FIXED_TERM_NOT_FOUND));

        fixedTermDeposits.getAccount().setBalance(fixedTermDeposits.getAccount().getBalance() + fixedTermDeposits.getAmount());
        fixedTermDeposits.setClosingDate(null);
        fixedTermDepositsRepository.save(fixedTermDeposits);

        return FixedTermDepositsResponseDTO.builder()
                .amount(fixedTermDeposits.getAmount())
                .interest(fixedTermDeposits.getInterest())
                .closingDate(null)
                .creationDate(LocalDate.from(fixedTermDeposits.getCreationDate()))
                .total(fixedTermDeposits.getAmount())
                .build();
    }
}
package com.bbva.wallet.utils;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Configuration
public class Utils {

    @Autowired
    private AccountRepository accountRepository;

    public String generateRandomCbu() {

        String randomNumber = generateUniqueCbuNumber(22);

        List<String> existingCbus = accountRepository.findAll().stream()
                .map(Account::getCbu)
                .toList();

        String cbu = randomNumber;

        while (existingCbus.contains(cbu)) {
            randomNumber = generateUniqueCbuNumber(22);
            cbu = randomNumber;
        }

        return cbu;
    }

    private String generateUniqueCbuNumber(int length) {
        return new Random().ints(length, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(""));
    }
}
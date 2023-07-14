package com.bbva.wallet.utils;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Page<Transaction> paginateTransactions(Iterable<Transaction> transactions, int page, int size) {
        List<Transaction> transactionList = StreamSupport.stream(transactions.spliterator(), false)
                .collect(Collectors.toList());

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, transactionList.size());

        List<Transaction> pageTransactions = transactionList.subList(startIndex, endIndex);

        return new PageImpl<>(pageTransactions, PageRequest.of(page, size), transactionList.size());
    }

}
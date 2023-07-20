package com.bbva.wallet.seeder;

import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TypeTransaction;
import com.bbva.wallet.repositories.AccountRepository;
import com.bbva.wallet.repositories.TransactionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionSeeder {
    private final AccountSeeder accountSeeder;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private final String[] descriptions = {
            "Gastos de Compras",
            "Salario",
            "Salida a Restaurantes",
            "Compras de Supermercado",
            "Pago por Freelance",
            "Costo de Transporte",
            "Gastos de Vacaciones",
            "Premio de Lotería",
            "Pago de Servicios",
            "Ganancias de Inversión",
            "Entretenimiento",
            "Ingresos por Alquiler",
            "Gastos de Salud",
            "Honorarios de Educación",
            "Ganancias de Negocio",
            "Donación Caritativa",
            "Prima de Seguro",
            "Gastos de Viaje",
            "Dividendos de Inversión",
            "Mantenimiento del Hogar",
            "Salida a Restaurantes",
            "Ingresos por Alquiler",
            "Entretenimiento",
            "Gastos de Vacaciones",
            "Ganancias de Negocio",
            "Gastos de Salud"
    };

    private final TypeTransaction[] types = {
            TypeTransaction.INCOME,
            TypeTransaction.PAYMENT,
            TypeTransaction.DEPOSIT
    };
    @PostConstruct
    public void seedTransactions() {
        if(transactionRepository.count() != 0) {
            return;
        }
        List<Account> accounts = accountSeeder.seedAccounts();

        accounts.forEach(this::makeTransaction);
    }

    private void makeTransaction(Account account) {
        Double randomAmount = Math.round((new Random().nextDouble() * account.getBalance() ) * 100.0) / 100.0;
        int randomTypeIndex = new Random().nextInt(types.length);

        createTransaction(
                account,
                randomAmount,
                descriptions[new Random().nextInt(descriptions.length)],
                types[randomTypeIndex]);

        account.setBalance(
                types[randomTypeIndex].equals(TypeTransaction.PAYMENT) ?
                account.getBalance() - randomAmount :
                account.getBalance() + randomAmount);
        accountRepository.save(account);
    }

    private void createTransaction(Account account, Double amount, String description, TypeTransaction type) {
        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .type(type)
                .description(description)
                .build();
        transactionRepository.save(transaction);
    }
}
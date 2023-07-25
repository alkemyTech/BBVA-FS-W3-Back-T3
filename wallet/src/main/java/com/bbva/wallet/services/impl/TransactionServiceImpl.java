package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.TransactionCreatedResponse;
import com.bbva.wallet.dtos.TransactionRequestDTO;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TypeTransaction;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.repositories.TransactionRepository;
import com.bbva.wallet.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction send(TransactionRequestDTO transactionDto, Account sourceAccount, Account destinationAccount) throws TransactionException {
        if (sourceAccount.getUser().equals(destinationAccount.getUser())) {
            throw new TransactionException("No se puede transferir a uno mismo", ErrorCodes.SAME_ACCOUNT_TRANSFER);
        }
        if (transactionDto.getAmount() > sourceAccount.getTransactionLimit()) {
            throw new TransactionException("No se puede transferir mas del limite", ErrorCodes.OVER_LIMIT);
        }
        if (transactionDto.getAmount() > sourceAccount.getBalance()) {
            throw new TransactionException("No se puede transferir mas de lo que se tiene", ErrorCodes.INSUFFICIENT_FOUNDS);
        }
        if (!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
            throw new TransactionException("No se puede transferir a una cuenta de distinta moneda", ErrorCodes.DIFFERENT_CURRENCY);
        }

        var income = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.INCOME)
                .account(destinationAccount)
                .description(transactionDto.getDescription() != null ? transactionDto.getDescription() : "")
                .build();
        var payment = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.PAYMENT)
                .account(sourceAccount)
                .description(transactionDto.getDescription() != null ? transactionDto.getDescription() : "")
                .build();

        transactionRepository.save(income);
        transactionRepository.save(payment);

        Double newBalanceIncome = destinationAccount.getBalance() + transactionDto.getAmount();
        Double newBalancePayment = sourceAccount.getBalance() - transactionDto.getAmount();
        destinationAccount.setBalance(newBalanceIncome);
        sourceAccount.setBalance(newBalancePayment);
        return payment;
    }

    // ------------------------------------------Deposit--------------------------------------------------------------
    public TransactionCreatedResponse deposit(Account account, Double amount, String description) throws TransactionException {
        return getTransactionCreatedResponse(account, amount, TypeTransaction.DEPOSIT, description);
    }

    //  ------------------------------------------Payment--------------------------------------------------------------
    public TransactionCreatedResponse payment(Account sourceAccount, Double amount,String description) throws TransactionException {
        if (amount > sourceAccount.getBalance()) {
            throw new TransactionException("No se puede realizar un pago con mas dinero del que se tiene", ErrorCodes.INSUFFICIENT_FOUNDS);
        }
        return getTransactionCreatedResponse(sourceAccount, amount, TypeTransaction.PAYMENT,description);
    }

    private TransactionCreatedResponse getTransactionCreatedResponse(Account account, Double amount,
                                                                     TypeTransaction typeTransaction, String description) throws TransactionException {
        if (amount <= 0) {
            throw new TransactionException("El monto ingresado debe ser mayor a 0", ErrorCodes.INCORRECT_AMOUNT);
        }

        Transaction transactions = Transaction.builder()
                .account(account)
                .type(typeTransaction)
                .amount(amount)
                .description(description)
                .build();

        account.setBalance(account.getBalance() + (typeTransaction.equals(TypeTransaction.DEPOSIT) ? amount : -amount ) );

        transactionRepository.save(transactions);

        return TransactionCreatedResponse.builder()
                .accountId(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .amount(amount)
                .description(description)
                .build();
    }

    @Override
    public Optional<Transaction> findById(Long Id) {
        return transactionRepository.findById(Id);
    }

    public Transaction save( Transaction transaction ){
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getUserTransaction(List<Account> accounts) {
        List<Transaction> transactions = new ArrayList<>();
        accounts.forEach(account ->
            transactions.addAll(transactionRepository.findByAccountId(account.getId())
            ));
        return transactions;
    }
}
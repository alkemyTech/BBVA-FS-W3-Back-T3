package com.bbva.wallet.services.impl;

import com.bbva.wallet.dtos.DepositCreatedDTO;
import com.bbva.wallet.dtos.TransactionDto;
import com.bbva.wallet.entities.Account;
import com.bbva.wallet.entities.Transaction;
import com.bbva.wallet.enums.TypeTransaction;
import com.bbva.wallet.exeptions.ErrorCodes;
import com.bbva.wallet.exeptions.TransactionException;
import com.bbva.wallet.repositories.TransactionsRepository;
import com.bbva.wallet.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionsRepository transactionsRepository;

    public Transaction send(TransactionDto transactionDto, Account sourceAccount, Account destinationAccount ) throws TransactionException {
        if (sourceAccount.getUser().equals(destinationAccount.getUser())){
            throw new TransactionException("No se puede transferir a uno mismo", ErrorCodes.SAME_ACCOUNT_TRANSFER);
        }
        if (transactionDto.getAmount() > sourceAccount.getTransactionLimit()){
            throw new TransactionException("No se puede transferir mas del limite", ErrorCodes.OVER_LIMIT);
        }
        if (transactionDto.getAmount() > sourceAccount.getBalance()){
            throw new TransactionException("No se puede transferir mas de lo que se tiene", ErrorCodes.INSUFFICIENT_FOUNDS);
        }
        if(!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())){
            throw new TransactionException("No se puede transferir a una cuenta de distinta moneda", ErrorCodes.DIFFERENT_CURRENCY);
        }


        var income = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.INCOME)
                .account(destinationAccount)
                .build();
        var payment = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(TypeTransaction.PAYMENT)
                .account(sourceAccount)
                .build();

        transactionsRepository.save(income);
        transactionsRepository.save(payment);

        Double newBalanceIncome = destinationAccount.getBalance() + transactionDto.getAmount();
        Double newBalancePayment = sourceAccount.getBalance() - transactionDto.getAmount();
        destinationAccount.setBalance(newBalanceIncome);
        sourceAccount.setBalance(newBalancePayment);
        return payment;
    }

    // ------------------------------------------Deposit--------------------------------------------------------------
    public DepositCreatedDTO deposit(Account account, double amount) throws TransactionException{
        if (amount <= 0) {
            throw new TransactionException("El monto ingresado debe ser mayor a 0", ErrorCodes.INSUFFICIENT_FOUNDS);
        }
        // Crear una nueva transacción
        Transaction transactions = Transaction.builder()
                .account(account)
                .type(TypeTransaction.DEPOSIT)
                .amount(amount)
                .build();

        // Actualizar el balance de la cuenta
        account.setBalance(account.getBalance() + amount);

        // Guardar la transacción y actualizar la cuenta en la base de datos
        transactionsRepository.save(transactions);

        return DepositCreatedDTO.builder()
                .accountId(account.getId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .amount(amount)
                .build();
    }
}
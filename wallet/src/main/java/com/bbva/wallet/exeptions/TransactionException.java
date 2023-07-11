package com.bbva.wallet.exeptions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TransactionException extends Throwable{
    private String message;
    private  ErrorCodes errorCodes;






}

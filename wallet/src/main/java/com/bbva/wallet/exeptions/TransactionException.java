package com.bbva.wallet.exeptions;

public class TransactionException extends BaseException{

    public TransactionException(String message, ErrorCodes errorCodes) {
        super(message, errorCodes);
    }
}

package com.bbva.wallet.exeptions;

public class AccountException extends BaseException{
    public AccountException(String message, ErrorCodes errorCodes) {
        super(message, errorCodes);
    }
}

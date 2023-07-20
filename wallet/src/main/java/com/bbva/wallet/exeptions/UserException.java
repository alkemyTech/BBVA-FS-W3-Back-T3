package com.bbva.wallet.exeptions;

public class UserException extends BaseException {
    public UserException(String message, ErrorCodes errorCodes) {
        super(message, errorCodes);
    }
}

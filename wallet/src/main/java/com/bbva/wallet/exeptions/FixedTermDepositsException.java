package com.bbva.wallet.exeptions;

public class FixedTermDepositsException extends BaseException{
    public FixedTermDepositsException(String message, ErrorCodes errorCodes) {
        super(message, errorCodes);
    }
}

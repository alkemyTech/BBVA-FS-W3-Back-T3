package com.bbva.wallet.exeptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseException extends Throwable{
    private String message;
    private  ErrorCodes errorCodes;
}

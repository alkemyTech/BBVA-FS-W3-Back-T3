package com.bbva.wallet.exeptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AccountException extends Throwable{
    private String message;
    private  ErrorCodes errorCodes;

}

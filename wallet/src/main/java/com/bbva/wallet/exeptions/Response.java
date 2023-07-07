package com.bbva.wallet.exeptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Response<T> {
    private List<ErrorCodes> errors;
    private String message;
    private T data;

    public void addError(ErrorCodes error) {
        if (Objects.isNull(this.errors)) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}



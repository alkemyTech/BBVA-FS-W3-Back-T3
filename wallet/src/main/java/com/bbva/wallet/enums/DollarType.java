package com.bbva.wallet.enums;


import lombok.Getter;

@Getter

public enum DollarType {
    OFICIAL("oficial"),
    BLUE("blue"),
    OFICIAL_EURO("oficial_euro"),
    BLUE_EURO("blue_euro");

    private final String type;

    DollarType(String type) {
        this.type = type;
    }


}
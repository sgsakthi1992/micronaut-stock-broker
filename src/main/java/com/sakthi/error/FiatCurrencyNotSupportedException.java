package com.sakthi.error;

public class FiatCurrencyNotSupportedException extends RuntimeException {
    public FiatCurrencyNotSupportedException(String msg) {
        super(msg);
    }
}

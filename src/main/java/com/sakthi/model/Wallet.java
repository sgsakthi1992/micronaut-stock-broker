package com.sakthi.model;

import com.sakthi.api.RestApiResponse;
import com.sakthi.error.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked
) implements RestApiResponse {

    public Wallet addAvailable(BigDecimal amountToAdd) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToAdd),
                this.locked
        );
    }

    public Wallet reduceAvailable(BigDecimal amountToSubtract) {
        if (this.available().compareTo(amountToSubtract) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in wallet id: " + this.walletId());
        }
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.subtract(amountToSubtract),
                this.locked
        );
    }
}

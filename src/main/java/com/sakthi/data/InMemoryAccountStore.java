package com.sakthi.data;

import com.sakthi.model.DepositFiatMoney;
import com.sakthi.model.Wallet;
import com.sakthi.model.WatchList;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class InMemoryAccountStore {
    public static final UUID ACCOUNT_ID = UUID.fromString("f4245629-83df-4ed8-90d9-7401045b5921");

    private final Map<UUID, WatchList> watchListPerAccount = new HashMap<>();
    private final Map<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();

    public WatchList getWatchList(UUID accountId) {
        return watchListPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(UUID accountId, WatchList watchList) {
        watchListPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchList(UUID accountId) {
        watchListPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletsPerAccount.get(accountId))
                .orElse(new HashMap<>())
                .values();
    }

    public Wallet depositToWallet(DepositFiatMoney deposit) {
        var wallets = Optional.ofNullable(walletsPerAccount.get(deposit.accountId()))
                .orElse(new HashMap<>());
        var oldWallet = Optional.ofNullable(wallets.get(deposit.walletId()))
                .orElse(new Wallet(ACCOUNT_ID, deposit.walletId(), deposit.symbol(), BigDecimal.ZERO, BigDecimal.ZERO));
        var newWallet = oldWallet.addAvailable(deposit.amount());
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);
        return newWallet;
    }
}

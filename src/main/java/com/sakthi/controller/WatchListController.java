package com.sakthi.controller;

import com.sakthi.data.InMemoryAccountStore;
import com.sakthi.model.WatchList;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Put;

import java.util.UUID;

@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {

    static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Void> delete() {
        store.deleteWatchList(ACCOUNT_ID);
        return HttpResponse.noContent();
    }
}

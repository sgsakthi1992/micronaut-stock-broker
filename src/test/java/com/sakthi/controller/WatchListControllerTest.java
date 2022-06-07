package com.sakthi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.data.InMemoryAccountStore;
import com.sakthi.model.Symbol;
import com.sakthi.model.WatchList;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Stream;

import static io.micronaut.http.HttpRequest.GET;
import static io.micronaut.http.HttpRequest.PUT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class WatchListControllerTest {

    private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;
    private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);

    @Inject
    @Client("/account/watchlist")
    HttpClient client;

    @Inject
    InMemoryAccountStore inMemoryAccountStore;

    @BeforeEach
    void setUp() {
        inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }

    @Test
    void testReturnsEmptyWatchListForTestAccount() {
        var response = client.toBlocking().retrieve(GET("/"), WatchList.class);
        assertNull(response.symbols());
        assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }

    @Test
    void testReturnsWatchListForTestAccount() {
        inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
                Stream.of("AAPL", "GOOGL", "MSFT")
                        .map(Symbol::new)
                        .toList()
        ));

        var response = client.toBlocking().exchange("/", JsonNode.class);
        System.out.println(response.body().toPrettyString());
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatus()),
                () -> assertEquals("{\"symbols\":[{\"value\":\"AAPL\"},{\"value\":\"GOOGL\"},{\"value\":\"MSFT\"}]}", response.getBody().get().toString())
        );
    }

    @Test
    void testUpdateWatchListForTestAccount() {
        var symbols = Stream.of("AAPL", "GOOGL", "MSFT")
                .map(Symbol::new)
                .toList();
        var request = PUT("/", new WatchList(symbols)).accept(MediaType.APPLICATION_JSON);

        var response = client.toBlocking().exchange(request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatus()),
                () -> assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols())
        );
    }
}
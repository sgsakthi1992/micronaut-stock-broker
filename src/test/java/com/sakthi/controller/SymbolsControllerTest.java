package com.sakthi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.sakthi.data.InMemoryStore;
import com.sakthi.model.Symbol;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@MicronautTest
class SymbolsControllerTest {

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    void setUp() {
        inMemoryStore.initWith(10);
    }

    @Test
    @DisplayName("Symbols endpoint should return list of symbols")
    void testGetAll() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertEquals(10, response.getBody().get().size())
        );
    }

    @Test
    @DisplayName("Symbols endpoint should return the correct symbol")
    void testGetSymbolByValue() {
        var testSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(testSymbol.value(), testSymbol);

        var response = client.toBlocking().exchange("/"+testSymbol.value(), Symbol.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertEquals(testSymbol, response.getBody().get())
        );
    }
}
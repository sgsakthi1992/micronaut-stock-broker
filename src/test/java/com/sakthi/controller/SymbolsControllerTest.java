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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


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

        var response = client.toBlocking().exchange("/" + testSymbol.value(), Symbol.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertEquals(testSymbol, response.getBody().get())
        );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    @DisplayName("Symbols filter endpoint should return the exact count")
    void testGetSymbolsWithFilter(Integer max, Integer offset, Integer expected) {
        var response = client.toBlocking().exchange("/filter?max=" + max + "&offset=" + offset, JsonNode.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertEquals(expected, response.getBody().get().size())
        );
    }

    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments(5, 5, 5),
                arguments(2, 0, 2),
                arguments(0, 5, 0),
                arguments(5, 7, 3),
                arguments(1, 1, 1),
                arguments(null, 1, 9),
                arguments(5, null, 5)
        );
    }

}
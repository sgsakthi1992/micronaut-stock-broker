package com.sakthi.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@MicronautTest
class SymbolsControllerTest {

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Test
    @DisplayName("Symbols endpoint should return list of symbols")
    void testGetAll() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertEquals(10, response.getBody().get().size())
        );
    }
}
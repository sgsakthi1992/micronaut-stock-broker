package com.sakthi.controller;

import com.sakthi.data.InMemoryStore;
import com.sakthi.model.Symbol;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller("/symbols")
public class SymbolsController {

    private final InMemoryStore inMemoryStore;

    public SymbolsController(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    @Get
    public List<Symbol> getAll() {
        return new ArrayList<>(inMemoryStore.getSymbols().values());
    }

    @Get("{value}")
    public Symbol getSymbolByValue(String value) {
        return inMemoryStore.getSymbols().get(value);
    }

    @Get("/filter{?max,offset}")
    public List<Symbol> getSymbolsWithFilter(@QueryValue Optional<Integer> max, @QueryValue Optional<Integer> offset) {
        return inMemoryStore.getSymbols().values().stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(10))
                .toList();
    }
}

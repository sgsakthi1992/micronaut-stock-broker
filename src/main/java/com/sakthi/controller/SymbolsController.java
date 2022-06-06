package com.sakthi.controller;

import com.sakthi.data.InMemoryStore;
import com.sakthi.model.Symbol;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
}

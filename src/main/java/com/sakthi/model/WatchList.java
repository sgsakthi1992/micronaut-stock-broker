package com.sakthi.model;

import java.util.ArrayList;
import java.util.List;

public record WatchList(List<Symbol> symbols) {
    public WatchList() {
        this(new ArrayList<>());
    }
}

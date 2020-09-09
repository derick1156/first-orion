package com.firstorion.project.DerickMaloneVertxApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Instrument {

    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;
    private String name;

    public Instrument(String name) {
        this.id = COUNTER.getAndIncrement();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

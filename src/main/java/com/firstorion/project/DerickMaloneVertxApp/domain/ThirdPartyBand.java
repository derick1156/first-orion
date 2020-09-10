package com.firstorion.project.DerickMaloneVertxApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThirdPartyBand {

    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;
    private String bandName;

    public ThirdPartyBand(String bandName) {
        this.id = COUNTER.getAndIncrement();
        this.bandName = bandName;
    }

    public ThirdPartyBand() {
        this.id = COUNTER.getAndIncrement();
    }

    public int getId() {
        return id;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

}

package com.firstorion.project.DerickMaloneVertxApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {

    //todo dm understand this
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;
    private String firstName;
    private String lastName;
    private ArrayList<Instrument> instruments;

    public Artist(String firstName, String lastName) {
        this.id = COUNTER.getAndIncrement();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Artist(String firstName, String lastName, ArrayList<Instrument> instruments) {
        this.id = COUNTER.getAndIncrement();
        this.firstName = firstName;
        this.lastName = lastName;
        this.instruments = instruments;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(ArrayList<Instrument> instruments) {
        this.instruments = instruments;
    }
}

package com.firstorion.project.DerickMaloneVertxApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.concurrent.atomic.AtomicInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Song {

    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;
    private String title;

    public Song(String title) {
        this.id = COUNTER.getAndIncrement();
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

package com.firstorion.project.DerickMaloneVertxApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

//todo dm make sure these annotations are necessary
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Band implements Serializable {

    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;
    private String name;
//    private ArrayList<Artist> artists;

    public Band(String name) {
        this.id = COUNTER.getAndIncrement();
        this.name = name;
    }

    public Band() {
        this.id = COUNTER.getAndIncrement();
    }

    //    public Band(String name, ArrayList<Artist> artists) {
//        this.id = COUNTER.getAndIncrement();
//        this.name = name;
//        this.artists = artists;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Band band = (Band) o;
        return name.equalsIgnoreCase(band.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
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

    //    public List<Artist> getArtists() {
//        return artists;
//    }
//
//    public void setArtists(ArrayList<Artist> artists) {
//        this.artists = artists;
//    }
}

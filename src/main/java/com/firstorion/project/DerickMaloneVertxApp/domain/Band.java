package com.firstorion.project.DerickMaloneVertxApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

//todo dm make sure these annotations are necessary
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Band implements Serializable {

    //todo dm understand this
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final int id;
    private String name;

    public Band(String name) {
        this.id = COUNTER.getAndIncrement();
        this.name = name;
    }

    public Band() {
        this.id = COUNTER.getAndIncrement();
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

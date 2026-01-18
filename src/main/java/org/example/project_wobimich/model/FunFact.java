package org.example.project_wobimich.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FunFact {
    @JsonProperty("ID-Fact")
    private int id;
    @JsonProperty("Kategorie")
    private String category;
    @JsonProperty("Fakt")
    private String fact;

    public FunFact(){};

    public int getId() {
        return this.id;
    }

    public String getFact() {
        return this.fact;
    }
}

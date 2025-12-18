package org.example.project_wobimich.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FunFact {
    @JsonProperty("ID-Fact")
    private int id;
    @JsonProperty("Kategorie")
    private String category;
    @JsonProperty("Fakt")
    private String fact;

    public FunFact(int id, String category, String fact) {
        this.id = id;
        this.category = category;
        this.fact = fact;
    }

    public FunFact(){};

    public int getId() {
        return this.id;
    }

    public String getCategory() {
        return this.category;
    }

    public String getFact() {
        return this.fact;
    }
}

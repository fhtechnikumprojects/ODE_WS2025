package org.example.project_wobimich.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a fun fact retrieved from an external data source (src/main/resources/org/example/project_wobimich/data/wl-fun-facts.json).
 * <p>
 * Contains an ID, a category and the fact text itself.
 */
public class FunFact {
    @JsonProperty("ID-Fact")
    private int id;
    @JsonProperty("Kategorie")
    private String category;
    @JsonProperty("Fakt")
    private String fact;

    /**
     * Default constructor required for Jackson deserialization.
     */
    public FunFact(){};

    /**
     * Returns the ID of the fun fact.
     *
     * @return fun fact ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the fun fact text.
     *
     * @return fun fact text
     */
    public String getFact() {
        return this.fact;
    }

    /**
     *
     * Return fun-fact of this object.
     *
     * @return fun fact text
     */
    @Override
    public String toString() {
        return this.getFact();
    }

}

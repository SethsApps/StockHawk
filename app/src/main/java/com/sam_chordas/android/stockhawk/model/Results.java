package com.sam_chordas.android.stockhawk.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "quote"
})
public class Results {

    @JsonProperty("quote")
    private List<Quote> quote = new ArrayList<Quote>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Results() {
    }

    /**
     *
     * @param quote
     */
    public Results(List<Quote> quote) {
        this.quote = quote;
    }

    /**
     *
     * @return
     * The quote
     */
    @JsonProperty("quote")
    public List<Quote> getQuote() {
        return quote;
    }

    /**
     *
     * @param quote
     * The quote
     */
    @JsonProperty("quote")
    public void setQuote(List<Quote> quote) {
        this.quote = quote;
    }

    public Results withQuote(List<Quote> quote) {
        this.quote = quote;
        return this;
    }

}
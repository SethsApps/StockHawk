package com.sam_chordas.android.stockhawk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "query"
})
public class HistoricalData {

    @JsonProperty("query")
    private Query query;

    /**
     * No args constructor for use in serialization
     *
     */
    public HistoricalData() {
    }

    /**
     *
     * @param query
     */
    public HistoricalData(Query query) {
        this.query = query;
    }

    /**
     *
     * @return
     * The query
     */
    @JsonProperty("query")
    public Query getQuery() {
        return query;
    }

    /**
     *
     * @param query
     * The query
     */
    @JsonProperty("query")
    public void setQuery(Query query) {
        this.query = query;
    }

    public HistoricalData withQuery(Query query) {
        this.query = query;
        return this;
    }

}
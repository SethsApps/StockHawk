package com.sam_chordas.android.stockhawk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Symbol",
        "Date",
        "Open",
        "High",
        "Low",
        "Close",
        "Volume",
        "Adj_Close"
})
public class Quote {

    @JsonProperty("Symbol")
    private String symbol;
    @JsonProperty("Date")
    private String date;
    @JsonProperty("Open")
    private String open;
    @JsonProperty("High")
    private String high;
    @JsonProperty("Low")
    private String low;
    @JsonProperty("Close")
    private String close;
    @JsonProperty("Volume")
    private String volume;
    @JsonProperty("Adj_Close")
    private String adjClose;

    /**
     * No args constructor for use in serialization
     *
     */
    public Quote() {
    }

    /**
     *
     * @param open
     * @param adjClose
     * @param symbol
     * @param volume
     * @param high
     * @param low
     * @param date
     * @param close
     */
    public Quote(String symbol, String date, String open, String high, String low, String close, String volume, String adjClose) {
        this.symbol = symbol;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adjClose = adjClose;
    }

    /**
     *
     * @return
     * The symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @param symbol
     * The Symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Quote withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    public Quote withDate(String date) {
        this.date = date;
        return this;
    }

    /**
     *
     * @return
     * The open
     */
    public String getOpen() {
        return open;
    }

    /**
     *
     * @param open
     * The Open
     */
    public void setOpen(String open) {
        this.open = open;
    }

    public Quote withOpen(String open) {
        this.open = open;
        return this;
    }

    /**
     *
     * @return
     * The high
     */
    public String getHigh() {
        return high;
    }

    /**
     *
     * @param high
     * The High
     */
    public void setHigh(String high) {
        this.high = high;
    }

    public Quote withHigh(String high) {
        this.high = high;
        return this;
    }

    /**
     *
     * @return
     * The low
     */
    public String getLow() {
        return low;
    }

    /**
     *
     * @param low
     * The Low
     */
    public void setLow(String low) {
        this.low = low;
    }

    public Quote withLow(String low) {
        this.low = low;
        return this;
    }

    /**
     *
     * @return
     * The close
     */
    public String getClose() {
        return close;
    }

    /**
     *
     * @param close
     * The Close
     */
    public void setClose(String close) {
        this.close = close;
    }

    public Quote withClose(String close) {
        this.close = close;
        return this;
    }

    /**
     *
     * @return
     * The volume
     */
    public String getVolume() {
        return volume;
    }

    /**
     *
     * @param volume
     * The Volume
     */
    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Quote withVolume(String volume) {
        this.volume = volume;
        return this;
    }

    /**
     *
     * @return
     * The adjClose
     */
    public String getAdjClose() {
        return adjClose;
    }

    /**
     *
     * @param adjClose
     * The Adj_Close
     */
    public void setAdjClose(String adjClose) {
        this.adjClose = adjClose;
    }

    public Quote withAdjClose(String adjClose) {
        this.adjClose = adjClose;
        return this;
    }

}
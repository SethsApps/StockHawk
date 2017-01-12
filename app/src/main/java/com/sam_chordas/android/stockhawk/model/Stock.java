package com.sam_chordas.android.stockhawk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Stock implements Parcelable {

    public String symbol;
    public String bidPrice;
    public String change;
    public boolean isUp;
    public String stockQuoteContentDescription;

    public Stock(String symbol, String bidPrice, String change, boolean isUp, String stockQuoteContentDescription) {
        this.symbol     = symbol;
        this.bidPrice   = bidPrice;
        this.change     = change;
        this.isUp       = isUp;
        this.stockQuoteContentDescription = stockQuoteContentDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.symbol);
        dest.writeString(this.bidPrice);
        dest.writeString(this.change);
        dest.writeByte(this.isUp ? (byte) 1 : (byte) 0);
        dest.writeString(this.stockQuoteContentDescription);
    }

    protected Stock(Parcel in) {
        this.symbol = in.readString();
        this.bidPrice = in.readString();
        this.change = in.readString();
        this.isUp = in.readByte() != 0;
        this.stockQuoteContentDescription = in.readString();
    }

    public static final Parcelable.Creator<Stock> CREATOR = new Parcelable.Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel source) {
            return new Stock(source);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };
}